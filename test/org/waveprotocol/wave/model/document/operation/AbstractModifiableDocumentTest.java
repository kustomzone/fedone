// Copyright 2009 Google Inc. All Rights Reserved.

package org.waveprotocol.wave.model.document.operation;

import junit.framework.TestCase;

import org.waveprotocol.wave.model.document.operation.impl.AnnotationBoundaryMapImpl;
import org.waveprotocol.wave.model.document.operation.impl.AttributesImpl;
import org.waveprotocol.wave.model.document.operation.impl.AttributesUpdateImpl;
import org.waveprotocol.wave.model.document.operation.impl.DocOpBuffer;
import org.waveprotocol.wave.model.document.operation.impl.DocOpUtil;
import org.waveprotocol.wave.model.operation.OperationException;

public abstract class AbstractModifiableDocumentTest<D extends ModifiableDocument>
    extends TestCase {

  abstract protected D createEmptyDocument();

  abstract protected DocInitialization asInitialization(D document);


  public static final DocOp TEST_DOC1 = new DocOpBuffer() {
      {
        // Check the 3 things we need to escape
        AnnotationBoundaryMapImpl link1 = AnnotationBoundaryMapImpl.builder()
            .initializationValues("link", "12?\"\\3").build();
        AnnotationBoundaryMapImpl link2 = AnnotationBoundaryMapImpl.builder()
            .initializationValues("link", "1").build();
        AnnotationBoundaryMapImpl ann = AnnotationBoundaryMapImpl.builder()
            .initializationValues("x", "3", "y", "3").build();
        AnnotationBoundaryMapImpl linkNull = AnnotationBoundaryMapImpl.builder()
            .initializationEnd("link").build();
        AnnotationBoundaryMapImpl change = AnnotationBoundaryMapImpl.builder()
            .initializationValues("xb", "5", "xc", "6", "z", "4", "zz", "7")
            .initializationEnd("x").build();
        AnnotationBoundaryMapImpl finish = AnnotationBoundaryMapImpl.builder()
            .initializationEnd("xb", "xc", "y", "z", "zz").build();
        elementStart("p", new AttributesImpl());
        characters("hi ");
        characters("therW");
        elementStart("q", new AttributesImpl("a", "1"));
        // Check things we need to escape
        characters("<some>markup&");
        elementEnd();

        // Check things we need to escape
        elementStart("r", new AttributesImpl("a", "2", "b", "\\\"'"));
        elementEnd();

        elementStart("q", Attributes.EMPTY_MAP);
        annotationBoundary(link1);
        elementEnd();
        elementStart("q", Attributes.EMPTY_MAP);
        annotationBoundary(AnnotationBoundaryMapImpl.builder().build());
        elementEnd();
        elementStart("q", Attributes.EMPTY_MAP);
        annotationBoundary(link1);
        elementEnd();

        elementStart("r", Attributes.EMPTY_MAP);
        annotationBoundary(link1);
        elementEnd();
        elementStart("r", Attributes.EMPTY_MAP);
        annotationBoundary(link2);
        elementEnd();

        annotationBoundary(ann);
        characters("abc");
        annotationBoundary(linkNull);
        characters("def");
        annotationBoundary(change);
        characters("ghi");

        annotationBoundary(finish);

        elementEnd();
      }
    }.finish();

  public static final String TEST_DOC1_XML =
      "<p>hi therW" +
      "<q a=\"1\">&lt;some&gt;markup&amp;</q>" +
      "<r a=\"2\" b=\"\\&quot;'\"/>" +
      "<q><?a \"link\"=\"12\\q\\\"\\\\3\"?></q>" +
      "<q/>" +
      "<q/>" +
      "<r/>" +
      "<r><?a \"link\"=\"1\"?></r>" +
      "<?a \"x\"=\"3\" \"y\"=\"3\"?>abc" +
      "<?a \"link\"?>def" +
      "<?a \"x\" \"xb\"=\"5\" \"xc\"=\"6\" \"z\"=\"4\" \"zz\"=\"7\"?>ghi" +
      "<?a \"xb\" \"xc\" \"y\" \"z\" \"zz\"?>" +
      "</p>";

  public void testDoc1() throws OperationException {
    D doc = createEmptyDocument();
    doc.consume(TEST_DOC1);
    check(TEST_DOC1_XML, doc);

    doc.consume(new DocOpBuffer() {
      {
        updateAttributes(new AttributesUpdateImpl("a", null, "2"));
        deleteCharacters("hi t");
        characters("you ");
        retain(4);
        replaceAttributes(new AttributesImpl("a", "1"), new AttributesImpl("b", "2"));
        retain("<some>markup&".length() + 1);
        updateAttributes(new AttributesUpdateImpl("a", "2", "3"));
        retain(2);
        annotationBoundary(AnnotationBoundaryMapImpl.builder().updateValues(
            "link", "12?\"\\3", "blah").build());
        retain(3);
        annotationBoundary(AnnotationBoundaryMapImpl.builder().initializationEnd("link").build());
        retain(16);
      }
    }.finish());

    check(
        "<p a=\"2\">you herW" +
        "<q b=\"2\">&lt;some&gt;markup&amp;</q>" +
        "<r a=\"3\" b=\"\\&quot;'\"/><q><?a \"link\"=\"blah\"?>" +
        "</q><q/><?a \"link\"=\"12\\q\\\"\\\\3\"?>" +
        "<q/>" +
        "<r/>" +
        "<r><?a \"link\"=\"1\"?></r>" +
        "<?a \"x\"=\"3\" \"y\"=\"3\"?>abc" +
        "<?a \"link\"?>def" +
        "<?a \"x\" \"xb\"=\"5\" \"xc\"=\"6\" \"z\"=\"4\" \"zz\"=\"7\"?>ghi" +
        "<?a \"xb\" \"xc\" \"y\" \"z\" \"zz\"?>" +
        "</p>",
        doc);
  }

  protected void check(String xml, D doc) throws OperationException {
    assertEquals(
        xml,
        DocOpUtil.toXmlString(asInitialization(doc))
    );
    D copy = createEmptyDocument();
    copy.consume(asInitialization(doc));
    assertEquals(
        xml,
        DocOpUtil.toXmlString(asInitialization(copy))
    );
  }
}
