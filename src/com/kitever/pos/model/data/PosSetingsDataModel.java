package com.kitever.pos.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PosSetingsDataModel {

@SerializedName("SettingID")
@Expose
private Integer settingID;
@SerializedName("InvoicePrefix")
@Expose
private String invoicePrefix;
@SerializedName("OrderPrefix")
@Expose
private String orderPrefix;
@SerializedName("TinNo")
@Expose
private String tinNo;
@SerializedName("PanNo")
@Expose
private String panNo;
@SerializedName("ServicetaxNo")
@Expose
private String servicetaxNo;
@SerializedName("MailTemplateId")
@Expose
private String mailTemplateId;
@SerializedName("SmsTemplateId")
@Expose
private String smsTemplateId;
@SerializedName("Invoice_No_Start")
@Expose
private String invoiceNoStart;
@SerializedName("Receipt_No_Start")
@Expose
private String receiptNoStart;
@SerializedName("OrderDefaultTaxApplicable")
@Expose
private String orderDefaultTaxApplicable;
@SerializedName("OrderSMSCustomer")
@Expose
private String orderSMSCustomer;
@SerializedName("OrderEmailCustomer")
@Expose
private String orderEmailCustomer;
@SerializedName("OrderSMSTime")
@Expose
private String orderSMSTime;
@SerializedName("OrderMailTime")
@Expose
private String orderMailTime;
@SerializedName("OrdermailtimeDelay")
@Expose
private String ordermailtimeDelay;
@SerializedName("OrdermailDaysDelay")
@Expose
private String ordermailDaysDelay;
@SerializedName("OrdersmstimeDelay")
@Expose
private String ordersmstimeDelay;
@SerializedName("OrdersmsDaysDelay")
@Expose
private String ordersmsDaysDelay;
@SerializedName("MailTemplateIdPayment")
@Expose
private String mailTemplateIdPayment;
@SerializedName("SmsTemplateIdPayment")
@Expose
private String smsTemplateIdPayment;
@SerializedName("PaymentSMSCustomer")
@Expose
private String paymentSMSCustomer;
@SerializedName("PaymentEmailCustomer")
@Expose
private String paymentEmailCustomer;
@SerializedName("PaymentSMSTime")
@Expose
private String paymentSMSTime;
@SerializedName("PaymentMailTime")
@Expose
private String paymentMailTime;
@SerializedName("PaymentmailtimeDelay")
@Expose
private String paymentmailtimeDelay;
@SerializedName("PaymentmailDaysDelay")
@Expose
private String paymentmailDaysDelay;
@SerializedName("PaymentsmstimeDelay")
@Expose
private String paymentsmstimeDelay;
@SerializedName("PaymentsmsDaysDelay")
@Expose
private String paymentsmsDaysDelay;
@SerializedName("MailTemplateIdCredit")
@Expose
private String mailTemplateIdCredit;
@SerializedName("SmsTemplateIdCredit")
@Expose
private String smsTemplateIdCredit;
@SerializedName("CreditSMSCustomer")
@Expose
private String creditSMSCustomer;
@SerializedName("CreditEmailCustomer")
@Expose
private String creditEmailCustomer;
@SerializedName("CreditSMSTime")
@Expose
private String creditSMSTime;
@SerializedName("CreditMailTime")
@Expose
private String creditMailTime;
@SerializedName("AfterDueDateChecked")
@Expose
private String afterDueDateChecked;
@SerializedName("DueDateMailRadio")
@Expose
private String dueDateMailRadio;
@SerializedName("AfterDueDateTime")
@Expose
private String afterDueDateTime;
@SerializedName("AfterDueDatePeriod")
@Expose
private String afterDueDatePeriod;
@SerializedName("CreatedDate")
@Expose
private String createdDate;
@SerializedName("UserID")
@Expose
private Integer userID;
@SerializedName("IsActive")
@Expose
private String isActive;

/**
* No args constructor for use in serialization
* 
*/
public PosSetingsDataModel() {
}

/**
* 
* @param receiptNoStart
* @param tinNo
* @param smsTemplateIdCredit
* @param creditEmailCustomer
* @param ordermailtimeDelay
* @param creditMailTime
* @param invoicePrefix
* @param ordermailDaysDelay
* @param smsTemplateId
* @param paymentMailTime
* @param orderSMSTime
* @param paymentsmsDaysDelay
* @param orderDefaultTaxApplicable
* @param ordersmstimeDelay
* @param dueDateMailRadio
* @param creditSMSCustomer
* @param ordersmsDaysDelay
* @param afterDueDateTime
* @param orderPrefix
* @param createdDate
* @param orderMailTime
* @param paymentsmstimeDelay
* @param servicetaxNo
* @param creditSMSTime
* @param paymentmailtimeDelay
* @param settingID
* @param orderEmailCustomer
* @param paymentEmailCustomer
* @param paymentSMSCustomer
* @param afterDueDatePeriod
* @param invoiceNoStart
* @param mailTemplateId
* @param isActive
* @param userID
* @param panNo
* @param paymentSMSTime
* @param smsTemplateIdPayment
* @param afterDueDateChecked
* @param orderSMSCustomer
* @param mailTemplateIdPayment
* @param paymentmailDaysDelay
* @param mailTemplateIdCredit
*/
public PosSetingsDataModel(Integer settingID, String invoicePrefix, String orderPrefix, String tinNo, String panNo, String servicetaxNo, String mailTemplateId, String smsTemplateId, String invoiceNoStart, String receiptNoStart, String orderDefaultTaxApplicable, String orderSMSCustomer, String orderEmailCustomer, String orderSMSTime, String orderMailTime, String ordermailtimeDelay, String ordermailDaysDelay, String ordersmstimeDelay, String ordersmsDaysDelay, String mailTemplateIdPayment, String smsTemplateIdPayment, String paymentSMSCustomer, String paymentEmailCustomer, String paymentSMSTime, String paymentMailTime, String paymentmailtimeDelay, String paymentmailDaysDelay, String paymentsmstimeDelay, String paymentsmsDaysDelay, String mailTemplateIdCredit, String smsTemplateIdCredit, String creditSMSCustomer, String creditEmailCustomer, String creditSMSTime, String creditMailTime, String afterDueDateChecked, String dueDateMailRadio, String afterDueDateTime, String afterDueDatePeriod, String createdDate, Integer userID, String isActive) {
this.settingID = settingID;
this.invoicePrefix = invoicePrefix;
this.orderPrefix = orderPrefix;
this.tinNo = tinNo;
this.panNo = panNo;
this.servicetaxNo = servicetaxNo;
this.mailTemplateId = mailTemplateId;
this.smsTemplateId = smsTemplateId;
this.invoiceNoStart = invoiceNoStart;
this.receiptNoStart = receiptNoStart;
this.orderDefaultTaxApplicable = orderDefaultTaxApplicable;
this.orderSMSCustomer = orderSMSCustomer;
this.orderEmailCustomer = orderEmailCustomer;
this.orderSMSTime = orderSMSTime;
this.orderMailTime = orderMailTime;
this.ordermailtimeDelay = ordermailtimeDelay;
this.ordermailDaysDelay = ordermailDaysDelay;
this.ordersmstimeDelay = ordersmstimeDelay;
this.ordersmsDaysDelay = ordersmsDaysDelay;
this.mailTemplateIdPayment = mailTemplateIdPayment;
this.smsTemplateIdPayment = smsTemplateIdPayment;
this.paymentSMSCustomer = paymentSMSCustomer;
this.paymentEmailCustomer = paymentEmailCustomer;
this.paymentSMSTime = paymentSMSTime;
this.paymentMailTime = paymentMailTime;
this.paymentmailtimeDelay = paymentmailtimeDelay;
this.paymentmailDaysDelay = paymentmailDaysDelay;
this.paymentsmstimeDelay = paymentsmstimeDelay;
this.paymentsmsDaysDelay = paymentsmsDaysDelay;
this.mailTemplateIdCredit = mailTemplateIdCredit;
this.smsTemplateIdCredit = smsTemplateIdCredit;
this.creditSMSCustomer = creditSMSCustomer;
this.creditEmailCustomer = creditEmailCustomer;
this.creditSMSTime = creditSMSTime;
this.creditMailTime = creditMailTime;
this.afterDueDateChecked = afterDueDateChecked;
this.dueDateMailRadio = dueDateMailRadio;
this.afterDueDateTime = afterDueDateTime;
this.afterDueDatePeriod = afterDueDatePeriod;
this.createdDate = createdDate;
this.userID = userID;
this.isActive = isActive;
}

/**
* 
* @return
* The settingID
*/
public Integer getSettingID() {
return settingID;
}

/**
* 
* @param settingID
* The SettingID
*/
public void setSettingID(Integer settingID) {
this.settingID = settingID;
}

/**
* 
* @return
* The invoicePrefix
*/
public String getInvoicePrefix() {
return invoicePrefix;
}

/**
* 
* @param invoicePrefix
* The InvoicePrefix
*/
public void setInvoicePrefix(String invoicePrefix) {
this.invoicePrefix = invoicePrefix;
}

/**
* 
* @return
* The orderPrefix
*/
public String getOrderPrefix() {
return orderPrefix;
}

/**
* 
* @param orderPrefix
* The OrderPrefix
*/
public void setOrderPrefix(String orderPrefix) {
this.orderPrefix = orderPrefix;
}

/**
* 
* @return
* The tinNo
*/
public String getTinNo() {
return tinNo;
}

/**
* 
* @param tinNo
* The TinNo
*/
public void setTinNo(String tinNo) {
this.tinNo = tinNo;
}

/**
* 
* @return
* The panNo
*/
public String getPanNo() {
return panNo;
}

/**
* 
* @param panNo
* The PanNo
*/
public void setPanNo(String panNo) {
this.panNo = panNo;
}

/**
* 
* @return
* The servicetaxNo
*/
public String getServicetaxNo() {
return servicetaxNo;
}

/**
* 
* @param servicetaxNo
* The ServicetaxNo
*/
public void setServicetaxNo(String servicetaxNo) {
this.servicetaxNo = servicetaxNo;
}

/**
* 
* @return
* The mailTemplateId
*/
public String getMailTemplateId() {
return mailTemplateId;
}

/**
* 
* @param mailTemplateId
* The MailTemplateId
*/
public void setMailTemplateId(String mailTemplateId) {
this.mailTemplateId = mailTemplateId;
}

/**
* 
* @return
* The smsTemplateId
*/
public String getSmsTemplateId() {
return smsTemplateId;
}

/**
* 
* @param smsTemplateId
* The SmsTemplateId
*/
public void setSmsTemplateId(String smsTemplateId) {
this.smsTemplateId = smsTemplateId;
}

/**
* 
* @return
* The invoiceNoStart
*/
public String getInvoiceNoStart() {
return invoiceNoStart;
}

/**
* 
* @param invoiceNoStart
* The Invoice_No_Start
*/
public void setInvoiceNoStart(String invoiceNoStart) {
this.invoiceNoStart = invoiceNoStart;
}

/**
* 
* @return
* The receiptNoStart
*/
public String getReceiptNoStart() {
return receiptNoStart;
}

/**
* 
* @param receiptNoStart
* The Receipt_No_Start
*/
public void setReceiptNoStart(String receiptNoStart) {
this.receiptNoStart = receiptNoStart;
}

/**
* 
* @return
* The orderDefaultTaxApplicable
*/
public String getOrderDefaultTaxApplicable() {
return orderDefaultTaxApplicable;
}

/**
* 
* @param orderDefaultTaxApplicable
* The OrderDefaultTaxApplicable
*/
public void setOrderDefaultTaxApplicable(String orderDefaultTaxApplicable) {
this.orderDefaultTaxApplicable = orderDefaultTaxApplicable;
}

/**
* 
* @return
* The orderSMSCustomer
*/
public String getOrderSMSCustomer() {
return orderSMSCustomer;
}

/**
* 
* @param orderSMSCustomer
* The OrderSMSCustomer
*/
public void setOrderSMSCustomer(String orderSMSCustomer) {
this.orderSMSCustomer = orderSMSCustomer;
}

/**
* 
* @return
* The orderEmailCustomer
*/
public String getOrderEmailCustomer() {
return orderEmailCustomer;
}

/**
* 
* @param orderEmailCustomer
* The OrderEmailCustomer
*/
public void setOrderEmailCustomer(String orderEmailCustomer) {
this.orderEmailCustomer = orderEmailCustomer;
}

/**
* 
* @return
* The orderSMSTime
*/
public String getOrderSMSTime() {
return orderSMSTime;
}

/**
* 
* @param orderSMSTime
* The OrderSMSTime
*/
public void setOrderSMSTime(String orderSMSTime) {
this.orderSMSTime = orderSMSTime;
}

/**
* 
* @return
* The orderMailTime
*/
public String getOrderMailTime() {
return orderMailTime;
}

/**
* 
* @param orderMailTime
* The OrderMailTime
*/
public void setOrderMailTime(String orderMailTime) {
this.orderMailTime = orderMailTime;
}

/**
* 
* @return
* The ordermailtimeDelay
*/
public String getOrdermailtimeDelay() {
return ordermailtimeDelay;
}

/**
* 
* @param ordermailtimeDelay
* The OrdermailtimeDelay
*/
public void setOrdermailtimeDelay(String ordermailtimeDelay) {
this.ordermailtimeDelay = ordermailtimeDelay;
}

/**
* 
* @return
* The ordermailDaysDelay
*/
public String getOrdermailDaysDelay() {
return ordermailDaysDelay;
}

/**
* 
* @param ordermailDaysDelay
* The OrdermailDaysDelay
*/
public void setOrdermailDaysDelay(String ordermailDaysDelay) {
this.ordermailDaysDelay = ordermailDaysDelay;
}

/**
* 
* @return
* The ordersmstimeDelay
*/
public String getOrdersmstimeDelay() {
return ordersmstimeDelay;
}

/**
* 
* @param ordersmstimeDelay
* The OrdersmstimeDelay
*/
public void setOrdersmstimeDelay(String ordersmstimeDelay) {
this.ordersmstimeDelay = ordersmstimeDelay;
}

/**
* 
* @return
* The ordersmsDaysDelay
*/
public String getOrdersmsDaysDelay() {
return ordersmsDaysDelay;
}

/**
* 
* @param ordersmsDaysDelay
* The OrdersmsDaysDelay
*/
public void setOrdersmsDaysDelay(String ordersmsDaysDelay) {
this.ordersmsDaysDelay = ordersmsDaysDelay;
}

/**
* 
* @return
* The mailTemplateIdPayment
*/
public String getMailTemplateIdPayment() {
return mailTemplateIdPayment;
}

/**
* 
* @param mailTemplateIdPayment
* The MailTemplateIdPayment
*/
public void setMailTemplateIdPayment(String mailTemplateIdPayment) {
this.mailTemplateIdPayment = mailTemplateIdPayment;
}

/**
* 
* @return
* The smsTemplateIdPayment
*/
public String getSmsTemplateIdPayment() {
return smsTemplateIdPayment;
}

/**
* 
* @param smsTemplateIdPayment
* The SmsTemplateIdPayment
*/
public void setSmsTemplateIdPayment(String smsTemplateIdPayment) {
this.smsTemplateIdPayment = smsTemplateIdPayment;
}

/**
* 
* @return
* The paymentSMSCustomer
*/
public String getPaymentSMSCustomer() {
return paymentSMSCustomer;
}

/**
* 
* @param paymentSMSCustomer
* The PaymentSMSCustomer
*/
public void setPaymentSMSCustomer(String paymentSMSCustomer) {
this.paymentSMSCustomer = paymentSMSCustomer;
}

/**
* 
* @return
* The paymentEmailCustomer
*/
public String getPaymentEmailCustomer() {
return paymentEmailCustomer;
}

/**
* 
* @param paymentEmailCustomer
* The PaymentEmailCustomer
*/
public void setPaymentEmailCustomer(String paymentEmailCustomer) {
this.paymentEmailCustomer = paymentEmailCustomer;
}

/**
* 
* @return
* The paymentSMSTime
*/
public String getPaymentSMSTime() {
return paymentSMSTime;
}

/**
* 
* @param paymentSMSTime
* The PaymentSMSTime
*/
public void setPaymentSMSTime(String paymentSMSTime) {
this.paymentSMSTime = paymentSMSTime;
}

/**
* 
* @return
* The paymentMailTime
*/
public String getPaymentMailTime() {
return paymentMailTime;
}

/**
* 
* @param paymentMailTime
* The PaymentMailTime
*/
public void setPaymentMailTime(String paymentMailTime) {
this.paymentMailTime = paymentMailTime;
}

/**
* 
* @return
* The paymentmailtimeDelay
*/
public String getPaymentmailtimeDelay() {
return paymentmailtimeDelay;
}

/**
* 
* @param paymentmailtimeDelay
* The PaymentmailtimeDelay
*/
public void setPaymentmailtimeDelay(String paymentmailtimeDelay) {
this.paymentmailtimeDelay = paymentmailtimeDelay;
}

/**
* 
* @return
* The paymentmailDaysDelay
*/
public String getPaymentmailDaysDelay() {
return paymentmailDaysDelay;
}

/**
* 
* @param paymentmailDaysDelay
* The PaymentmailDaysDelay
*/
public void setPaymentmailDaysDelay(String paymentmailDaysDelay) {
this.paymentmailDaysDelay = paymentmailDaysDelay;
}

/**
* 
* @return
* The paymentsmstimeDelay
*/
public String getPaymentsmstimeDelay() {
return paymentsmstimeDelay;
}

/**
* 
* @param paymentsmstimeDelay
* The PaymentsmstimeDelay
*/
public void setPaymentsmstimeDelay(String paymentsmstimeDelay) {
this.paymentsmstimeDelay = paymentsmstimeDelay;
}

/**
* 
* @return
* The paymentsmsDaysDelay
*/
public String getPaymentsmsDaysDelay() {
return paymentsmsDaysDelay;
}

/**
* 
* @param paymentsmsDaysDelay
* The PaymentsmsDaysDelay
*/
public void setPaymentsmsDaysDelay(String paymentsmsDaysDelay) {
this.paymentsmsDaysDelay = paymentsmsDaysDelay;
}

/**
* 
* @return
* The mailTemplateIdCredit
*/
public String getMailTemplateIdCredit() {
return mailTemplateIdCredit;
}

/**
* 
* @param mailTemplateIdCredit
* The MailTemplateIdCredit
*/
public void setMailTemplateIdCredit(String mailTemplateIdCredit) {
this.mailTemplateIdCredit = mailTemplateIdCredit;
}

/**
* 
* @return
* The smsTemplateIdCredit
*/
public String getSmsTemplateIdCredit() {
return smsTemplateIdCredit;
}

/**
* 
* @param smsTemplateIdCredit
* The SmsTemplateIdCredit
*/
public void setSmsTemplateIdCredit(String smsTemplateIdCredit) {
this.smsTemplateIdCredit = smsTemplateIdCredit;
}

/**
* 
* @return
* The creditSMSCustomer
*/
public String getCreditSMSCustomer() {
return creditSMSCustomer;
}

/**
* 
* @param creditSMSCustomer
* The CreditSMSCustomer
*/
public void setCreditSMSCustomer(String creditSMSCustomer) {
this.creditSMSCustomer = creditSMSCustomer;
}

/**
* 
* @return
* The creditEmailCustomer
*/
public String getCreditEmailCustomer() {
return creditEmailCustomer;
}

/**
* 
* @param creditEmailCustomer
* The CreditEmailCustomer
*/
public void setCreditEmailCustomer(String creditEmailCustomer) {
this.creditEmailCustomer = creditEmailCustomer;
}

/**
* 
* @return
* The creditSMSTime
*/
public String getCreditSMSTime() {
return creditSMSTime;
}

/**
* 
* @param creditSMSTime
* The CreditSMSTime
*/
public void setCreditSMSTime(String creditSMSTime) {
this.creditSMSTime = creditSMSTime;
}

/**
* 
* @return
* The creditMailTime
*/
public String getCreditMailTime() {
return creditMailTime;
}

/**
* 
* @param creditMailTime
* The CreditMailTime
*/
public void setCreditMailTime(String creditMailTime) {
this.creditMailTime = creditMailTime;
}

/**
* 
* @return
* The afterDueDateChecked
*/
public String getAfterDueDateChecked() {
return afterDueDateChecked;
}

/**
* 
* @param afterDueDateChecked
* The AfterDueDateChecked
*/
public void setAfterDueDateChecked(String afterDueDateChecked) {
this.afterDueDateChecked = afterDueDateChecked;
}

/**
* 
* @return
* The dueDateMailRadio
*/
public String getDueDateMailRadio() {
return dueDateMailRadio;
}

/**
* 
* @param dueDateMailRadio
* The DueDateMailRadio
*/
public void setDueDateMailRadio(String dueDateMailRadio) {
this.dueDateMailRadio = dueDateMailRadio;
}

/**
* 
* @return
* The afterDueDateTime
*/
public String getAfterDueDateTime() {
return afterDueDateTime;
}

/**
* 
* @param afterDueDateTime
* The AfterDueDateTime
*/
public void setAfterDueDateTime(String afterDueDateTime) {
this.afterDueDateTime = afterDueDateTime;
}

/**
* 
* @return
* The afterDueDatePeriod
*/
public String getAfterDueDatePeriod() {
return afterDueDatePeriod;
}

/**
* 
* @param afterDueDatePeriod
* The AfterDueDatePeriod
*/
public void setAfterDueDatePeriod(String afterDueDatePeriod) {
this.afterDueDatePeriod = afterDueDatePeriod;
}

/**
* 
* @return
* The createdDate
*/
public String getCreatedDate() {
return createdDate;
}

/**
* 
* @param createdDate
* The CreatedDate
*/
public void setCreatedDate(String createdDate) {
this.createdDate = createdDate;
}

/**
* 
* @return
* The userID
*/
public Integer getUserID() {
return userID;
}

/**
* 
* @param userID
* The UserID
*/
public void setUserID(Integer userID) {
this.userID = userID;
}

/**
* 
* @return
* The isActive
*/
public String getIsActive() {
return isActive;
}

/**
* 
* @param isActive
* The IsActive
*/
public void setIsActive(String isActive) {
this.isActive = isActive;
}

}