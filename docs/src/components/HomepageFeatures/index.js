import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';
import { Hammer, Code, Layers } from 'lucide-react'; // Correct icon imports

// List of features
const FeatureList = [
    {
        title: 'Job Handling Framework',
        Icon: Hammer,
        description: (
            <>
                Simplifies job management with generic solutions for scheduling, monitoring, and error handling.
            </>
        ),
    },
    {
        title: 'Code Generator',
        Icon: Code,
        description: (
            <>
                Automatically generates boilerplate code for new job handlers, saving time and reducing errors.
            </>
        ),
    },
    {
        title: 'Extensible and Modular',
        Icon: Layers,
        description: (
            <>
                Designed for easy customization and extension to fit various business needs.
            </>
        ),
    },
];

// Feature Component
function Feature({ Icon, title, description }) {
    return (
        <div className={clsx('col col--4')}>
            <div className="text--center">
                <Icon size={40} /> {/* Use Icon component directly */}
            </div>
            <div className="text--center padding-horiz--md">
                <h3>{title}</h3>
                <p>{description}</p>
            </div>
        </div>
    );
}

// HomepageFeatures Component
export default function HomepageFeatures() {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}