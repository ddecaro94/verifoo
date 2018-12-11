//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.11 at 01:17:29 AM CET 
//


package it.polito.verifoo.rest.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}firewall"/>
 *         &lt;element ref="{}endhost"/>
 *         &lt;element ref="{}endpoint"/>
 *         &lt;element ref="{}antispam"/>
 *         &lt;element ref="{}cache"/>
 *         &lt;element ref="{}dpi"/>
 *         &lt;element ref="{}mailclient"/>
 *         &lt;element ref="{}mailserver"/>
 *         &lt;element ref="{}nat"/>
 *         &lt;element ref="{}vpnaccess"/>
 *         &lt;element ref="{}vpnexit"/>
 *         &lt;element ref="{}webclient"/>
 *         &lt;element ref="{}webserver"/>
 *         &lt;element ref="{}fieldmodifier"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "firewall",
    "endhost",
    "endpoint",
    "antispam",
    "cache",
    "dpi",
    "mailclient",
    "mailserver",
    "nat",
    "vpnaccess",
    "vpnexit",
    "webclient",
    "webserver",
    "fieldmodifier"
})
@XmlRootElement(name = "configuration")
public class Configuration {

    protected Firewall firewall;
    protected Endhost endhost;
    protected Endpoint endpoint;
    protected Antispam antispam;
    protected Cache cache;
    protected Dpi dpi;
    protected Mailclient mailclient;
    protected Mailserver mailserver;
    protected Nat nat;
    protected Vpnaccess vpnaccess;
    protected Vpnexit vpnexit;
    protected Webclient webclient;
    protected Webserver webserver;
    protected Fieldmodifier fieldmodifier;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the firewall property.
     * 
     * @return
     *     possible object is
     *     {@link Firewall }
     *     
     */
    public Firewall getFirewall() {
        return firewall;
    }

    /**
     * Sets the value of the firewall property.
     * 
     * @param value
     *     allowed object is
     *     {@link Firewall }
     *     
     */
    public void setFirewall(Firewall value) {
        this.firewall = value;
    }

    /**
     * Gets the value of the endhost property.
     * 
     * @return
     *     possible object is
     *     {@link Endhost }
     *     
     */
    public Endhost getEndhost() {
        return endhost;
    }

    /**
     * Sets the value of the endhost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Endhost }
     *     
     */
    public void setEndhost(Endhost value) {
        this.endhost = value;
    }

    /**
     * Gets the value of the endpoint property.
     * 
     * @return
     *     possible object is
     *     {@link Endpoint }
     *     
     */
    public Endpoint getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the value of the endpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Endpoint }
     *     
     */
    public void setEndpoint(Endpoint value) {
        this.endpoint = value;
    }

    /**
     * Gets the value of the antispam property.
     * 
     * @return
     *     possible object is
     *     {@link Antispam }
     *     
     */
    public Antispam getAntispam() {
        return antispam;
    }

    /**
     * Sets the value of the antispam property.
     * 
     * @param value
     *     allowed object is
     *     {@link Antispam }
     *     
     */
    public void setAntispam(Antispam value) {
        this.antispam = value;
    }

    /**
     * Gets the value of the cache property.
     * 
     * @return
     *     possible object is
     *     {@link Cache }
     *     
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets the value of the cache property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cache }
     *     
     */
    public void setCache(Cache value) {
        this.cache = value;
    }

    /**
     * Gets the value of the dpi property.
     * 
     * @return
     *     possible object is
     *     {@link Dpi }
     *     
     */
    public Dpi getDpi() {
        return dpi;
    }

    /**
     * Sets the value of the dpi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dpi }
     *     
     */
    public void setDpi(Dpi value) {
        this.dpi = value;
    }

    /**
     * Gets the value of the mailclient property.
     * 
     * @return
     *     possible object is
     *     {@link Mailclient }
     *     
     */
    public Mailclient getMailclient() {
        return mailclient;
    }

    /**
     * Sets the value of the mailclient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mailclient }
     *     
     */
    public void setMailclient(Mailclient value) {
        this.mailclient = value;
    }

    /**
     * Gets the value of the mailserver property.
     * 
     * @return
     *     possible object is
     *     {@link Mailserver }
     *     
     */
    public Mailserver getMailserver() {
        return mailserver;
    }

    /**
     * Sets the value of the mailserver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mailserver }
     *     
     */
    public void setMailserver(Mailserver value) {
        this.mailserver = value;
    }

    /**
     * Gets the value of the nat property.
     * 
     * @return
     *     possible object is
     *     {@link Nat }
     *     
     */
    public Nat getNat() {
        return nat;
    }

    /**
     * Sets the value of the nat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nat }
     *     
     */
    public void setNat(Nat value) {
        this.nat = value;
    }

    /**
     * Gets the value of the vpnaccess property.
     * 
     * @return
     *     possible object is
     *     {@link Vpnaccess }
     *     
     */
    public Vpnaccess getVpnaccess() {
        return vpnaccess;
    }

    /**
     * Sets the value of the vpnaccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vpnaccess }
     *     
     */
    public void setVpnaccess(Vpnaccess value) {
        this.vpnaccess = value;
    }

    /**
     * Gets the value of the vpnexit property.
     * 
     * @return
     *     possible object is
     *     {@link Vpnexit }
     *     
     */
    public Vpnexit getVpnexit() {
        return vpnexit;
    }

    /**
     * Sets the value of the vpnexit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vpnexit }
     *     
     */
    public void setVpnexit(Vpnexit value) {
        this.vpnexit = value;
    }

    /**
     * Gets the value of the webclient property.
     * 
     * @return
     *     possible object is
     *     {@link Webclient }
     *     
     */
    public Webclient getWebclient() {
        return webclient;
    }

    /**
     * Sets the value of the webclient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Webclient }
     *     
     */
    public void setWebclient(Webclient value) {
        this.webclient = value;
    }

    /**
     * Gets the value of the webserver property.
     * 
     * @return
     *     possible object is
     *     {@link Webserver }
     *     
     */
    public Webserver getWebserver() {
        return webserver;
    }

    /**
     * Sets the value of the webserver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Webserver }
     *     
     */
    public void setWebserver(Webserver value) {
        this.webserver = value;
    }

    /**
     * Gets the value of the fieldmodifier property.
     * 
     * @return
     *     possible object is
     *     {@link Fieldmodifier }
     *     
     */
    public Fieldmodifier getFieldmodifier() {
        return fieldmodifier;
    }

    /**
     * Sets the value of the fieldmodifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fieldmodifier }
     *     
     */
    public void setFieldmodifier(Fieldmodifier value) {
        this.fieldmodifier = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
