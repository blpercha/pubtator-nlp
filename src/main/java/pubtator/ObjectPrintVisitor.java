package pubtator;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ObjectPrintVisitor<T> implements Visitor<T> {
    private final PrintWriter printWriter;

    public ObjectPrintVisitor(OutputStream outputStream) {
        this.printWriter = new PrintWriter(outputStream);
    }

    @Override
    public void visit(T item) {
        printWriter.println(item); // uses item's own "toString" method
        printWriter.flush();
    }
}
