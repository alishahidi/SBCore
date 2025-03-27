package io.github.alishahidi.sbcore.pdf;

import io.github.alishahidi.sbcore.exception.ExceptionTemplate;
import io.github.alishahidi.sbcore.exception.ExceptionUtil;
import io.github.alishahidi.sbcore.util.ExecutorUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pdf {

    private Pdf() {
    }

    Integer count = 0;
    Integer select = null;
    PdfSelection selection = PdfSelection.ALL;

    public Pdf count(Integer count) {
        this.count = count;
        return this;
    }

    public Pdf select(Integer select) {
        this.select = select;
        return this;
    }

    public Pdf selection(PdfSelection selection) {
        this.selection = selection;
        return this;
    }

    private void reset() {
        count = 0;
        select = null;
        selection = PdfSelection.ALL;
    }

    public CompletableFuture<List<Path>> toImage(Path path) {
        return CompletableFuture.supplyAsync(() -> {
            List<Path> paths = new ArrayList<>();

            try (PDDocument document = PDDocument.load(path.toFile())) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int totalPages = document.getNumberOfPages();

                if(select != null){
                    Path selectPage = renderPage(pdfRenderer, select);
                    paths.add(selectPage);
                }else{
                    List<Integer> pageIndices = getPageIndices(totalPages);

                    paths = pageIndices.stream()
                            .map(page -> renderPage(pdfRenderer, page))
                            .toList();
                }

            } catch (IOException e) {
                throw new RuntimeException("Error processing PDF", e);
            }

            reset();
            return paths;
        }, ExecutorUtils.fixedThreadPool());
    }

    private List<Integer> getPageIndices(int totalPages) {
        int pageCount = Math.min(count != null && count > 0 ? count : totalPages, totalPages);

        return switch (selection) {
            case FIRST_N -> IntStream.range(0, pageCount).boxed().collect(Collectors.toList());
            case MIDDLE_N -> {
                int middleStart = Math.max(0, (totalPages / 2) - (pageCount / 2));
                yield IntStream.range(middleStart, middleStart + pageCount).boxed().collect(Collectors.toList());
            }
            case LAST_N ->
                    IntStream.range(Math.max(0, totalPages - pageCount), totalPages).boxed().collect(Collectors.toList());
            default -> IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
        };
    }

    private Path renderPage(PDFRenderer pdfRenderer, int page) {
        try {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
            Path tmpPath = Files.createTempFile(String.valueOf(page + 1), ".jpeg");
            ImageIO.write(bim, "JPEG", tmpPath.toFile());
            return tmpPath;
        } catch (IOException e) {
            throw ExceptionUtil.make(ExceptionTemplate.PDF_PAGE_ERROR_OR_NOT_FOUND);
        }
    }

    public static Pdf create() {
        return new Pdf();
    }
}
