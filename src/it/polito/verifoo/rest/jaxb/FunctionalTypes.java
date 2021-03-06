//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.11 at 01:17:29 AM CET 
//


package it.polito.verifoo.rest.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for functionalTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="functionalTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FIREWALL"/>
 *     &lt;enumeration value="ENDHOST"/>
 *     &lt;enumeration value="ENDPOINT"/>
 *     &lt;enumeration value="ANTISPAM"/>
 *     &lt;enumeration value="CACHE"/>
 *     &lt;enumeration value="DPI"/>
 *     &lt;enumeration value="MAILCLIENT"/>
 *     &lt;enumeration value="MAILSERVER"/>
 *     &lt;enumeration value="NAT"/>
 *     &lt;enumeration value="VPNACCESS"/>
 *     &lt;enumeration value="VPNEXIT"/>
 *     &lt;enumeration value="WEBCLIENT"/>
 *     &lt;enumeration value="WEBSERVER"/>
 *     &lt;enumeration value="FIELDMODIFIER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "functionalTypes")
@XmlEnum
public enum FunctionalTypes {

    FIREWALL,
    ENDHOST,
    ENDPOINT,
    ANTISPAM,
    CACHE,
    DPI,
    MAILCLIENT,
    MAILSERVER,
    NAT,
    VPNACCESS,
    VPNEXIT,
    WEBCLIENT,
    WEBSERVER,
    FIELDMODIFIER;

    public String value() {
        return name();
    }

    public static FunctionalTypes fromValue(String v) {
        return valueOf(v);
    }

}
