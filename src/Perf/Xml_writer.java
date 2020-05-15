/**
 *
 */
package Perf;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author liwenjie
 *
 */
public class Xml_writer{

		   public Document get_doc() throws Exception {
		       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		       DocumentBuilder builder = factory.newDocumentBuilder();
		       Document document = builder.newDocument();
		       return document;
		   }
		 //������Ԫ��
		   public Element begin(Document document,String name)throws Exception {
		       Element root = document.createElement(name);   //����
		       document.appendChild(root);
		       return root;
		   }
		 //����Ԫ��
		   public Element field(Document document,Element parent, String tagName, String text) throws Exception{
		       Element element = document.createElement(tagName);   //��ǩ��
		       if (text != null && text.length() > 0) {
		           element.setTextContent(text);
		       }
		       parent.appendChild(element);
		       return element;
		   }

		 //�����ļ�
		   public void toSave(Document document,String fileName) throws Exception {
		       TransformerFactory factory = TransformerFactory.newInstance();
		       factory.setAttribute("indent-number", new Integer(4));// ������������Ϊ4
		       Transformer transformer = factory.newTransformer();
		       transformer.setOutputProperty(OutputKeys.INDENT, "yes");// �����Զ�����
		       DOMSource source = new DOMSource(document);
		       transformer.transform(source, new StreamResult(new BufferedWriter(
		               new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))));
		   }


}
