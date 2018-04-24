//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.24 at 10:28:43 AM CEST 
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
 *     &lt;enumeration value="PDCP"/>
 *     &lt;enumeration value="SDCP"/>
 *     &lt;enumeration value="RLC"/>
 *     &lt;enumeration value="MAC"/>
 *     &lt;enumeration value="PHY"/>
 *     &lt;enumeration value="SGW"/>
 *     &lt;enumeration value="PGW"/>
 *     &lt;enumeration value="MME"/>
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
    FIELDMODIFIER,
    PDCP,
    SDCP,
    RLC,
    MAC,
    PHY,
    SGW,
    PGW,
    MME;

    public String value() {
        return name();
    }

    public static FunctionalTypes fromValue(String v) {
        return valueOf(v);
    }

}
