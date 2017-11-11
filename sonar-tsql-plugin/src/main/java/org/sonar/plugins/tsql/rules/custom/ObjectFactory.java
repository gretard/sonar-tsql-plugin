//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.11.11 at 07:28:36 PM EET 
//


package org.sonar.plugins.tsql.rules.custom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.sonar.plugins.tsql.rules.custom package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Severity_QNAME = new QName("", "severity");
    private final static QName _DebtRemediationFunctionCoefficient_QNAME = new QName("", "debtRemediationFunctionCoefficient");
    private final static QName _TextItem_QNAME = new QName("", "textItem");
    private final static QName _Description_QNAME = new QName("", "description");
    private final static QName _ClassName_QNAME = new QName("", "className");
    private final static QName _RemediationFunction_QNAME = new QName("", "remediationFunction");
    private final static QName _Source_QNAME = new QName("", "source");
    private final static QName _Cardinality_QNAME = new QName("", "cardinality");
    private final static QName _RuleViolationMessage_QNAME = new QName("", "ruleViolationMessage");
    private final static QName _RuleCodeExample_QNAME = new QName("", "ruleCodeExample");
    private final static QName _RemediationFunctionBaseEffort_QNAME = new QName("", "remediationFunctionBaseEffort");
    private final static QName _Name_QNAME = new QName("", "name");
    private final static QName _InternalKey_QNAME = new QName("", "internalKey");
    private final static QName _Tag_QNAME = new QName("", "tag");
    private final static QName _DescriptionFormat_QNAME = new QName("", "descriptionFormat");
    private final static QName _Key_QNAME = new QName("", "key");
    private final static QName _Status_QNAME = new QName("", "status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.sonar.plugins.tsql.rules.custom
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CompliantRulesCodeExamples }
     * 
     */
    public CompliantRulesCodeExamples createCompliantRulesCodeExamples() {
        return new CompliantRulesCodeExamples();
    }

    /**
     * Create an instance of {@link Rule }
     * 
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link RuleImplementation }
     * 
     */
    public RuleImplementation createRuleImplementation() {
        return new RuleImplementation();
    }

    /**
     * Create an instance of {@link Names }
     * 
     */
    public Names createNames() {
        return new Names();
    }

    /**
     * Create an instance of {@link TextToFind }
     * 
     */
    public TextToFind createTextToFind() {
        return new TextToFind();
    }

    /**
     * Create an instance of {@link ParentRules }
     * 
     */
    public ParentRules createParentRules() {
        return new ParentRules();
    }

    /**
     * Create an instance of {@link ChildrenRules }
     * 
     */
    public ChildrenRules createChildrenRules() {
        return new ChildrenRules();
    }

    /**
     * Create an instance of {@link SiblingsRules }
     * 
     */
    public SiblingsRules createSiblingsRules() {
        return new SiblingsRules();
    }

    /**
     * Create an instance of {@link UsesRules }
     * 
     */
    public UsesRules createUsesRules() {
        return new UsesRules();
    }

    /**
     * Create an instance of {@link ViolatingRulesCodeExamples }
     * 
     */
    public ViolatingRulesCodeExamples createViolatingRulesCodeExamples() {
        return new ViolatingRulesCodeExamples();
    }

    /**
     * Create an instance of {@link SqlRules }
     * 
     */
    public SqlRules createSqlRules() {
        return new SqlRules();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "severity")
    public JAXBElement<String> createSeverity(String value) {
        return new JAXBElement<String>(_Severity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "debtRemediationFunctionCoefficient")
    public JAXBElement<String> createDebtRemediationFunctionCoefficient(String value) {
        return new JAXBElement<String>(_DebtRemediationFunctionCoefficient_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "textItem")
    public JAXBElement<String> createTextItem(String value) {
        return new JAXBElement<String>(_TextItem_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "className")
    public JAXBElement<String> createClassName(String value) {
        return new JAXBElement<String>(_ClassName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "remediationFunction")
    public JAXBElement<String> createRemediationFunction(String value) {
        return new JAXBElement<String>(_RemediationFunction_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "source")
    public JAXBElement<String> createSource(String value) {
        return new JAXBElement<String>(_Source_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cardinality")
    public JAXBElement<String> createCardinality(String value) {
        return new JAXBElement<String>(_Cardinality_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ruleViolationMessage")
    public JAXBElement<String> createRuleViolationMessage(String value) {
        return new JAXBElement<String>(_RuleViolationMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ruleCodeExample")
    public JAXBElement<String> createRuleCodeExample(String value) {
        return new JAXBElement<String>(_RuleCodeExample_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "remediationFunctionBaseEffort")
    public JAXBElement<String> createRemediationFunctionBaseEffort(String value) {
        return new JAXBElement<String>(_RemediationFunctionBaseEffort_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "internalKey")
    public JAXBElement<String> createInternalKey(String value) {
        return new JAXBElement<String>(_InternalKey_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "tag")
    public JAXBElement<String> createTag(String value) {
        return new JAXBElement<String>(_Tag_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "descriptionFormat")
    public JAXBElement<String> createDescriptionFormat(String value) {
        return new JAXBElement<String>(_DescriptionFormat_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "key")
    public JAXBElement<String> createKey(String value) {
        return new JAXBElement<String>(_Key_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "status")
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

}
