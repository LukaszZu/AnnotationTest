package builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes({"builder.AzkabanJob"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AzkabanJobProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        set.forEach(c -> {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(c);
            elements.forEach(e -> {
                AzkabanJob job = e.getAnnotation(AzkabanJob.class);
                String s = String.format("%s - %s -> %s", job.descryption(), job.memory(), job.cpu());
                printWarn(e.getSimpleName() + " - " + s);
                try {
                    String packageNAme = processingEnv.getElementUtils().getPackageOf(e).getQualifiedName().toString();
                    FileObject resource = processingEnv.getFiler().createResource(
                            StandardLocation.CLASS_OUTPUT, "azkaban." + packageNAme, e.getSimpleName() + ".properties", e);

                    printWarn(packageNAme.toString());

                    try (Writer w = resource.openWriter()) {
                        w.write(s);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });

        return true;
    }

    private void printWarn(String s) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, s);
    }
}
