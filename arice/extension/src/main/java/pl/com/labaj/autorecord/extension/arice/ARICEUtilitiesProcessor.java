package pl.com.labaj.autorecord.extension.arice;

/*-
 * Copyright © 2023 Auto Record
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apiguardian.api.API;
import pl.com.labaj.autorecord.processor.context.MessagerLogger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static org.apiguardian.api.API.Status.MAINTAINED;

/**
 * {@inheritDoc}
 */
@API(status = MAINTAINED)
@SupportedAnnotationTypes("pl.com.labaj.autorecord.extension.arice.ARICEUtilities")
public class ARICEUtilitiesProcessor extends AbstractProcessor {
    private static final ExtensionContext extContext = new ExtensionContext();

    private Messager messager;
    private MethodsClassGenerator methodsClassGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        extContext.init(processingEnv);

        messager = processingEnv.getMessager();
        var filer = processingEnv.getFiler();

        methodsClassGenerator = new MethodsClassGenerator(extContext, filer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> processAnnotation(roundEnv, annotation));

        return false;
    }

    private void processAnnotation(RoundEnvironment roundEnv, TypeElement annotation) {
        var logger = new MessagerLogger(messager, annotation);

        roundEnv.getElementsAnnotatedWith(annotation).stream()
                .map(element -> element.getAnnotation(ARICEUtilities.class))
                .map(NameTypes::toNameTypes)
                .distinct()
                .forEach(nameTypes -> methodsClassGenerator.generate(nameTypes.className, nameTypes.immutableTypes, logger));
    }

    record NameTypes(String className, String[] immutableTypes) {
        private static NameTypes toNameTypes(ARICEUtilities a) {
            return new NameTypes(a.className(), a.immutableTypes());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NameTypes nameTypes)) return false;
            return className.equals(nameTypes.className) && Arrays.equals(immutableTypes, nameTypes.immutableTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(className);
            result = 31 * result + Arrays.hashCode(immutableTypes);
            return result;
        }

        @Override
        public String toString() {
            return "NameTypes{" +
                    "className='" + className + '\'' +
                    ", immutableTypes=" + Arrays.toString(immutableTypes) +
                    '}';
        }
    }
}