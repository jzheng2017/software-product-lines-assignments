package spl.texteditor.plugin.core.pf4j;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.reactfx.collection.ListModification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.plugin.core.pf4j.extensionpoints.TextAreaExtensionPoint;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSyntaxHighlightingPlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public JavaSyntaxHighlightingPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class JavaSyntaxHighlightingTextAreaExtensionPoint implements TextAreaExtensionPoint {
        private static final Logger LOGGER = LoggerFactory.getLogger(JavaSyntaxHighlightingPlugin.class);
        private static final String[] KEYWORDS = new String[]{
                "abstract", "assert", "boolean", "break", "byte",
                "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else",
                "enum", "extends", "final", "finally", "float",
                "for", "goto", "if", "implements", "import",
                "instanceof", "int", "interface", "long", "native",
                "new", "package", "private", "protected", "public",
                "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws",
                "transient", "try", "void", "volatile", "while"
        };

        private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        private static final String PAREN_PATTERN = "\\(|\\)";
        private static final String BRACE_PATTERN = "\\{|\\}";
        private static final String BRACKET_PATTERN = "\\[|\\]";
        private static final String SEMICOLON_PATTERN = "\\;";
        private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
                + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

        private static final Pattern PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );

        @Override
        public void extendTextArea(CodeArea textArea) {
            LOGGER.debug("Extending text area with Java syntax highlighting..");
            textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
//            textArea.setContextMenu(new DefaultContextMenu());

            textArea.getVisibleParagraphs().addModificationObserver
                    (
                            new VisibleParagraphStyler(textArea, new Function<String, StyleSpans>() {
                                @Override
                                public StyleSpans apply(String text) {
                                    return computeHighlighting(text);
                                }
                            })
                    );

            final Pattern whiteSpace = Pattern.compile("^\\s+");
            textArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        int caretPosition = textArea.getCaretPosition();
                        int currentParagraph = textArea.getCurrentParagraph();
                        Matcher m0 = whiteSpace.matcher(textArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                        if (m0.find()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    textArea.insertText(caretPosition, m0.group());
                                }
                            });
                        }
                    }
                }
            });

            textArea.getStylesheets().add("java-keywords.css");
        }

        private StyleSpans<Collection<String>> computeHighlighting(String text) {
            Matcher matcher = PATTERN.matcher(text);
            int lastKwEnd = 0;
            StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder();

            while (matcher.find()) {
                String styleClass =
                        matcher.group("KEYWORD") != null ? "keyword" :
                                matcher.group("PAREN") != null ? "paren" :
                                        matcher.group("BRACE") != null ? "brace" :
                                                matcher.group("BRACKET") != null ? "bracket" :
                                                        matcher.group("SEMICOLON") != null ? "semicolon" :
                                                                matcher.group("STRING") != null ? "string" :
                                                                        matcher.group("COMMENT") != null ? "comment" :
                                                                                null; /* never happens */
                assert styleClass != null;
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
                spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                lastKwEnd = matcher.end();
            }
            spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
            return spansBuilder.create();
        }

        private class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
            private final GenericStyledArea<PS, SEG, S> area;
            private final Function<String, StyleSpans<S>> computeStyles;
            private int prevParagraph, prevTextLength;

            public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles) {
                this.computeStyles = computeStyles;
                this.area = area;
            }

            @Override
            public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
                if (lm.getAddedSize() > 0) Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size() - 1);
                        String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

                        if (paragraph != prevParagraph || text.length() != prevTextLength) {
                            if (paragraph < area.getParagraphs().size() - 1) {
                                int startPos = area.getAbsolutePosition(paragraph, 0);
                                area.setStyleSpans(startPos, computeStyles.apply(text));
                            }
                            prevTextLength = text.length();
                            prevParagraph = paragraph;
                        }
                    }
                });
            }
        }

//        private class DefaultContextMenu extends ContextMenu {
//            private MenuItem fold, unfold, print;
//
//            public DefaultContextMenu() {
//                fold = new MenuItem("Fold selected text");
//                fold.setOnAction(AE -> {
//                    hide();
//                    fold();
//                });
//
//                unfold = new MenuItem("Unfold from cursor");
//                unfold.setOnAction(AE -> {
//                    hide();
//                    unfold();
//                });
//
//                print = new MenuItem("Print");
//                print.setOnAction(AE -> {
//                    hide();
//                    print();
//                });
//
//                getItems().addAll(fold, unfold, print);
//            }
//
//            /**
//             * Folds multiple lines of selected text, only showing the first line and hiding the rest.
//             */
//            private void fold() {
//                ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
//            }
//
//            /**
//             * Unfold the CURRENT line/paragraph if it has a fold.
//             */
//            private void unfold() {
//                CodeArea area = (CodeArea) getOwnerNode();
//                area.unfoldParagraphs(area.getCurrentParagraph());
//            }
//
//            private void print() {
//                System.out.println(((CodeArea) getOwnerNode()).getText());
//            }
//        }
    }
                @Override
	    public void start() {
		System.out.println("start");
	    }

	    @Override
	    public void stop() {
		System.out.println("stop");
	    }

	    @Override
	    public void delete() {
		System.out.println("delete");
	    }
}
