//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.11 at 01:17:29 AM CET 
//


package it.polito.verifoo.rest.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element name="BandwidthMetrics" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="dst" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="reqLatency" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bandwidthMetrics"
})
@XmlRootElement(name = "BandwidthConstraints")
public class BandwidthConstraints {

    @XmlElement(name = "BandwidthMetrics")
    protected List<BandwidthConstraints.BandwidthMetrics> bandwidthMetrics;

    /**
     * Gets the value of the bandwidthMetrics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bandwidthMetrics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBandwidthMetrics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BandwidthConstraints.BandwidthMetrics }
     * 
     * 
     */
    public List<BandwidthConstraints.BandwidthMetrics> getBandwidthMetrics() {
        if (bandwidthMetrics == null) {
            bandwidthMetrics = new ArrayList<BandwidthConstraints.BandwidthMetrics>();
        }
        return this.bandwidthMetrics;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="dst" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="reqLatency" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class BandwidthMetrics {

        @XmlAttribute(name = "src", required = true)
        protected String src;
        @XmlAttribute(name = "dst", required = true)
        protected String dst;
        @XmlAttribute(name = "reqLatency", required = true)
        protected int reqLatency;

        /**
         * Gets the value of the src property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSrc() {
            return src;
        }

        /**
         * Sets the value of the src property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSrc(String value) {
            this.src = value;
        }

        /**
         * Gets the value of the dst property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDst() {
            return dst;
        }

        /**
         * Sets the value of the dst property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDst(String value) {
            this.dst = value;
        }

        /**
         * Gets the value of the reqLatency property.
         * 
         */
        public int getReqLatency() {
            return reqLatency;
        }

        /**
         * Sets the value of the reqLatency property.
         * 
         */
        public void setReqLatency(int value) {
            this.reqLatency = value;
        }

    }

}
