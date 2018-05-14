//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.05.13 at 08:25:36 PM CEST 
//


package it.polito.verifoo.rest.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for P-Name.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="P-Name">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IsolationProperty"/>
 *     &lt;enumeration value="ReachabilityProperty"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "P-Name")
@XmlEnum
public enum PName {

    @XmlEnumValue("IsolationProperty")
    ISOLATION_PROPERTY("IsolationProperty"),
    @XmlEnumValue("ReachabilityProperty")
    REACHABILITY_PROPERTY("ReachabilityProperty");
    private final String value;

    PName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PName fromValue(String v) {
        for (PName c: PName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
