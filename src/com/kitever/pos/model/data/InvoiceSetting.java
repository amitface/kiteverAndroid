package com.kitever.pos.model.data;

public class InvoiceSetting {

    private String AfterDueDateChecked;

    private String OrdersmsDaysDelay;

    private String Template_Title;

    private String UserID;

    private String PaymentTemplate;

    private String PaymentsmstimeDelay;

    private String DueDateMailRadio;

    private String PaymentSMSTime;

    private String OrderEmailCustomer;

    private String SmsTemplateIdCredit;

    private String PaymentSMSCustomer;

    private String Mail_Payment_Template;

    private String PaymentsmsDaysDelay;

    private String CreditTemplate;

    private String IsActive;

    private String Mail_Template_Credit;

    private String PaymentmailtimeDelay;

    private String OrderPrefix;

    private String MailTemplateId;

    private String PanNo;

    private String OrderMailTime;

    private String PaymentMailTime;

    private String CreditEmailCustomer;

    private String ServicetaxNo;

    private String InvoicePrefix;

    private String SmsTemplateId;

    private String CreditSMSTime;

    private String Invoice_No_Start;

    private String OrderSMSCustomer;

    private String OrdersmstimeDelay;

    private String PaymentEmailCustomer;

    private String OrdermailDaysDelay;

    private String Mail_Template;

    private String MailTemplateIdCredit;

    private String Receipt_No_Start;

    private String OrderSMSTime;

    private String OrdermailtimeDelay;

    private String PaymentmailDaysDelay;

    private String SmsTemplateIdPayment;

    private String SettingID;

    private String CreatedDate;

    private String TinNo;

    private String CreditSMSCustomer;

    private String OrderDefaultTaxApplicable;

    private String MailTemplateIdPayment;

    private String AfterDueDatePeriod;

    private String AfterDueDateTime;

    private String CreditMailTime;
    private String iseditable;

    private String IsInvoiceHeader;
    private String IsReceiptHeader;
    private String IsInvoiceFooter;
    private String IsReceiptFooter;
    private String Header;
    private String Footer;
    private String AfterDueDateSMSChecked;
    private String DueDateSMSRadio;
    private String AfterDueDateSMSTime;
    private String AfterDueDateSMSPeriod;


    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public String getFooter() {
        return Footer;
    }

    public void setFooter(String footer) {
        Footer = footer;
    }

    public String getAfterDueDateSMSChecked() {
        return AfterDueDateSMSChecked;
    }

    public void setAfterDueDateSMSChecked(String afterDueDateSMSChecked) {
        AfterDueDateSMSChecked = afterDueDateSMSChecked;
    }

    public String getDueDateSMSRadio() {
        return DueDateSMSRadio;
    }

    public void setDueDateSMSRadio(String dueDateSMSRadio) {
        DueDateSMSRadio = dueDateSMSRadio;
    }

    public String getAfterDueDateSMSTime() {
        return AfterDueDateSMSTime;
    }

    public void setAfterDueDateSMSTime(String afterDueDateSMSTime) {
        AfterDueDateSMSTime = afterDueDateSMSTime;
    }

    public String getAfterDueDateSMSPeriod() {
        return AfterDueDateSMSPeriod;
    }

    public void setAfterDueDateSMSPeriod(String afterDueDateSMSPeriod) {
        AfterDueDateSMSPeriod = afterDueDateSMSPeriod;
    }




    public InvoiceSetting() {
        super();
        AfterDueDateChecked = "0";
        OrdersmsDaysDelay = "day";
        Template_Title = "";
        // UserID = userID;
        PaymentTemplate = "";
        PaymentsmstimeDelay = "0";
        DueDateMailRadio = "0";
        PaymentSMSTime = "0";
        OrderEmailCustomer = "0";
        SmsTemplateIdCredit = "0";
        PaymentSMSCustomer = "0";
        Mail_Payment_Template = "";
        PaymentsmsDaysDelay = "0";
        CreditTemplate = "0";
        IsActive = "0";
        Mail_Template_Credit = "";
        PaymentmailtimeDelay = "";
        OrderPrefix = "";
        MailTemplateId = "0";
        PanNo = "";
        OrderMailTime = "0";
        PaymentMailTime = "0";
        CreditEmailCustomer = "0";
        ServicetaxNo = "";
        InvoicePrefix = "";
        SmsTemplateId = "0";
        CreditSMSTime = "0";
        Invoice_No_Start = "";
        OrderSMSCustomer = "0";
        OrdersmstimeDelay = "0";
        PaymentEmailCustomer = "0";
        OrdermailDaysDelay = "day";
        Mail_Template = "0";
        MailTemplateIdCredit = "0";
        Receipt_No_Start = "";
        OrderSMSTime = "0";
        OrdermailtimeDelay = "0";
        PaymentmailDaysDelay = "day";
        SmsTemplateIdPayment = "0";
        SettingID = "0";
        CreatedDate = "0";
        TinNo = "";
        CreditSMSCustomer = "0";
        OrderDefaultTaxApplicable = "false";
        MailTemplateIdPayment = "0";
        AfterDueDatePeriod = "false";
        AfterDueDateTime = "0";
        CreditMailTime = "0";
        iseditable = "0";
        IsInvoiceHeader = "0";
        IsReceiptHeader = "0";
        IsInvoiceFooter = "0";
        IsReceiptFooter = "0";
        Header="";
        Footer="";
        AfterDueDateSMSChecked="0";
        DueDateSMSRadio="0";
        AfterDueDateSMSTime="0";
        AfterDueDateSMSPeriod="0";

    }

    public String getAfterDueDateChecked() {
        return AfterDueDateChecked;
    }

    public void setAfterDueDateChecked(String AfterDueDateChecked) {
        this.AfterDueDateChecked = AfterDueDateChecked;
    }

    public String getOrdersmsDaysDelay() {
        return OrdersmsDaysDelay;
    }

    public void setOrdersmsDaysDelay(String OrdersmsDaysDelay) {
        this.OrdersmsDaysDelay = OrdersmsDaysDelay;
    }

    public String getTemplate_Title() {
        return Template_Title;
    }

    public void setTemplate_Title(String Template_Title) {
        this.Template_Title = Template_Title;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getPaymentTemplate() {
        return PaymentTemplate;
    }

    public void setPaymentTemplate(String PaymentTemplate) {
        this.PaymentTemplate = PaymentTemplate;
    }

    public String getPaymentsmstimeDelay() {
        return PaymentsmstimeDelay;
    }

    public void setPaymentsmstimeDelay(String PaymentsmstimeDelay) {
        this.PaymentsmstimeDelay = PaymentsmstimeDelay;
    }

    public String getDueDateMailRadio() {
        return DueDateMailRadio;
    }

    public void setDueDateMailRadio(String DueDateMailRadio) {
        this.DueDateMailRadio = DueDateMailRadio;
    }

    public String getPaymentSMSTime() {
        return PaymentSMSTime;
    }

    public void setPaymentSMSTime(String PaymentSMSTime) {
        this.PaymentSMSTime = PaymentSMSTime;
    }

    public String getOrderEmailCustomer() {
        return OrderEmailCustomer;
    }

    public void setOrderEmailCustomer(String OrderEmailCustomer) {
        this.OrderEmailCustomer = OrderEmailCustomer;
    }

    public String getSmsTemplateIdCredit() {
        return SmsTemplateIdCredit;
    }

    public void setSmsTemplateIdCredit(String SmsTemplateIdCredit) {
        this.SmsTemplateIdCredit = SmsTemplateIdCredit;
    }

    public String getPaymentSMSCustomer() {
        return PaymentSMSCustomer;
    }

    public void setPaymentSMSCustomer(String PaymentSMSCustomer) {
        this.PaymentSMSCustomer = PaymentSMSCustomer;
    }

    public String getMail_Payment_Template() {
        return Mail_Payment_Template;
    }

    public void setMail_Payment_Template(String Mail_Payment_Template) {
        this.Mail_Payment_Template = Mail_Payment_Template;
    }

    public String getPaymentsmsDaysDelay() {
        return PaymentsmsDaysDelay;
    }

    public void setPaymentsmsDaysDelay(String PaymentsmsDaysDelay) {
        this.PaymentsmsDaysDelay = PaymentsmsDaysDelay;
    }

    public String getCreditTemplate() {
        return CreditTemplate;
    }

    public void setCreditTemplate(String CreditTemplate) {
        this.CreditTemplate = CreditTemplate;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String IsActive) {
        this.IsActive = IsActive;
    }

    public String getMail_Template_Credit() {
        return Mail_Template_Credit;
    }

    public void setMail_Template_Credit(String Mail_Template_Credit) {
        this.Mail_Template_Credit = Mail_Template_Credit;
    }

    public String getPaymentmailtimeDelay() {
        return PaymentmailtimeDelay;
    }

    public void setPaymentmailtimeDelay(String PaymentmailtimeDelay) {
        this.PaymentmailtimeDelay = PaymentmailtimeDelay;
    }

    public String getOrderPrefix() {
        return OrderPrefix;
    }

    public void setOrderPrefix(String OrderPrefix) {
        this.OrderPrefix = OrderPrefix;
    }

    public String getMailTemplateId() {
        return MailTemplateId;
    }

    public void setMailTemplateId(String MailTemplateId) {
        this.MailTemplateId = MailTemplateId;
    }

    public String getPanNo() {
        return PanNo;
    }

    public void setPanNo(String PanNo) {
        this.PanNo = PanNo;
    }

    public String getOrderMailTime() {
        return OrderMailTime;
    }

    public void setOrderMailTime(String OrderMailTime) {
        this.OrderMailTime = OrderMailTime;
    }

    public String getPaymentMailTime() {
        return PaymentMailTime;
    }

    public void setPaymentMailTime(String PaymentMailTime) {
        this.PaymentMailTime = PaymentMailTime;
    }

    public String getCreditEmailCustomer() {
        return CreditEmailCustomer;
    }

    public void setCreditEmailCustomer(String CreditEmailCustomer) {
        this.CreditEmailCustomer = CreditEmailCustomer;
    }

    public String getServicetaxNo() {
        return ServicetaxNo;
    }

    public void setServicetaxNo(String ServicetaxNo) {
        this.ServicetaxNo = ServicetaxNo;
    }

    public String getInvoicePrefix() {
        return InvoicePrefix;
    }

    public void setInvoicePrefix(String InvoicePrefix) {
        this.InvoicePrefix = InvoicePrefix;
    }

    public String getSmsTemplateId() {
        return SmsTemplateId;
    }

    public void setSmsTemplateId(String SmsTemplateId) {
        this.SmsTemplateId = SmsTemplateId;
    }

    public String getCreditSMSTime() {
        return CreditSMSTime;
    }

    public void setCreditSMSTime(String CreditSMSTime) {
        this.CreditSMSTime = CreditSMSTime;
    }

    public String getInvoice_No_Start() {
        return Invoice_No_Start;
    }

    public void setInvoice_No_Start(String Invoice_No_Start) {
        this.Invoice_No_Start = Invoice_No_Start;
    }

    public String getOrderSMSCustomer() {
        return OrderSMSCustomer;
    }

    public void setOrderSMSCustomer(String OrderSMSCustomer) {
        this.OrderSMSCustomer = OrderSMSCustomer;
    }

    public String getOrdersmstimeDelay() {
        return OrdersmstimeDelay;
    }

    public void setOrdersmstimeDelay(String OrdersmstimeDelay) {
        this.OrdersmstimeDelay = OrdersmstimeDelay;
    }

    public String getPaymentEmailCustomer() {
        return PaymentEmailCustomer;
    }

    public void setPaymentEmailCustomer(String PaymentEmailCustomer) {
        this.PaymentEmailCustomer = PaymentEmailCustomer;
    }

    public String getOrdermailDaysDelay() {
        return OrdermailDaysDelay;
    }

    public void setOrdermailDaysDelay(String OrdermailDaysDelay) {
        this.OrdermailDaysDelay = OrdermailDaysDelay;
    }

    public String getMail_Template() {
        return Mail_Template;
    }

    public void setMail_Template(String Mail_Template) {
        this.Mail_Template = Mail_Template;
    }

    public String getMailTemplateIdCredit() {
        return MailTemplateIdCredit;
    }

    public void setMailTemplateIdCredit(String MailTemplateIdCredit) {
        this.MailTemplateIdCredit = MailTemplateIdCredit;
    }

    public String getReceipt_No_Start() {
        return Receipt_No_Start;
    }

    public void setReceipt_No_Start(String Receipt_No_Start) {
        this.Receipt_No_Start = Receipt_No_Start;
    }

    public String getOrderSMSTime() {
        return OrderSMSTime;
    }

    public void setOrderSMSTime(String OrderSMSTime) {
        this.OrderSMSTime = OrderSMSTime;
    }

    public String getOrdermailtimeDelay() {
        return OrdermailtimeDelay;
    }

    public void setOrdermailtimeDelay(String OrdermailtimeDelay) {
        this.OrdermailtimeDelay = OrdermailtimeDelay;
    }

    public String getPaymentmailDaysDelay() {
        return PaymentmailDaysDelay;
    }

    public void setPaymentmailDaysDelay(String PaymentmailDaysDelay) {
        this.PaymentmailDaysDelay = PaymentmailDaysDelay;
    }

    public String getSmsTemplateIdPayment() {
        return SmsTemplateIdPayment;
    }

    public void setSmsTemplateIdPayment(String SmsTemplateIdPayment) {
        this.SmsTemplateIdPayment = SmsTemplateIdPayment;
    }

    public String getSettingID() {
        return SettingID;
    }

    public void setSettingID(String SettingID) {
        this.SettingID = SettingID;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getTinNo() {
        return TinNo;
    }

    public void setTinNo(String TinNo) {
        this.TinNo = TinNo;
    }

    public String getCreditSMSCustomer() {
        return CreditSMSCustomer;
    }

    public void setCreditSMSCustomer(String CreditSMSCustomer) {
        this.CreditSMSCustomer = CreditSMSCustomer;
    }

    public String getOrderDefaultTaxApplicable() {
        return OrderDefaultTaxApplicable;
    }

    public void setOrderDefaultTaxApplicable(String OrderDefaultTaxApplicable) {
        this.OrderDefaultTaxApplicable = OrderDefaultTaxApplicable;
    }

    public String getMailTemplateIdPayment() {
        return MailTemplateIdPayment;
    }

    public void setMailTemplateIdPayment(String MailTemplateIdPayment) {
        this.MailTemplateIdPayment = MailTemplateIdPayment;
    }

    public String getAfterDueDatePeriod() {
        return AfterDueDatePeriod;
    }

    public void setAfterDueDatePeriod(String AfterDueDatePeriod) {
        this.AfterDueDatePeriod = AfterDueDatePeriod;
    }

    public String getAfterDueDateTime() {
        return AfterDueDateTime;
    }

    public void setAfterDueDateTime(String AfterDueDateTime) {
        this.AfterDueDateTime = AfterDueDateTime;
    }

    public String getCreditMailTime() {
        return CreditMailTime;
    }

    public void setCreditMailTime(String CreditMailTime) {
        this.CreditMailTime = CreditMailTime;
    }

    public String getIseditable() {
        return iseditable;
    }

    public void setIseditable(String iseditable) {
        this.iseditable = iseditable;
    }

    public String getIsInvoiceHeader() {
        return IsInvoiceHeader;
    }

    public void setIsInvoiceHeader(String isInvoiceHeader) {
        IsInvoiceHeader = isInvoiceHeader;
    }

    public String getIsReceiptHeader() {
        return IsReceiptHeader;
    }

    public void setIsReceiptHeader(String isReceiptHeader) {
        IsReceiptHeader = isReceiptHeader;
    }

    public String getIsInvoiceFooter() {
        return IsInvoiceFooter;
    }

    public void setIsInvoiceFooter(String isInvoiceFooter) {
        IsInvoiceFooter = isInvoiceFooter;
    }

    public String getIsReceiptFooter() {
        return IsReceiptFooter;
    }

    public void setIsReceiptFooter(String isReceiptFooter) {
        IsReceiptFooter = isReceiptFooter;
    }

    @Override
    public String toString() {
        return "ClassPojo [AfterDueDateChecked = " + AfterDueDateChecked
                + ", OrdersmsDaysDelay = " + OrdersmsDaysDelay
                + ", Template_Title = " + Template_Title + ", UserID = "
                + UserID + ", PaymentTemplate = " + PaymentTemplate
                + ", PaymentsmstimeDelay = " + PaymentsmstimeDelay
                + ", DueDateMailRadio = " + DueDateMailRadio
                + ", PaymentSMSTime = " + PaymentSMSTime
                + ", OrderEmailCustomer = " + OrderEmailCustomer
                + ", SmsTemplateIdCredit = " + SmsTemplateIdCredit
                + ", PaymentSMSCustomer = " + PaymentSMSCustomer
                + ", Mail_Payment_Template = " + Mail_Payment_Template
                + ", PaymentsmsDaysDelay = " + PaymentsmsDaysDelay
                + ", CreditTemplate = " + CreditTemplate + ", IsActive = "
                + IsActive + ", Mail_Template_Credit = " + Mail_Template_Credit
                + ", PaymentmailtimeDelay = " + PaymentmailtimeDelay
                + ", OrderPrefix = " + OrderPrefix + ", MailTemplateId = "
                + MailTemplateId + ", PanNo = " + PanNo + ", OrderMailTime = "
                + OrderMailTime + ", PaymentMailTime = " + PaymentMailTime
                + ", CreditEmailCustomer = " + CreditEmailCustomer
                + ", ServicetaxNo = " + ServicetaxNo + ", InvoicePrefix = "
                + InvoicePrefix + ", SmsTemplateId = " + SmsTemplateId
                + ", CreditSMSTime = " + CreditSMSTime
                + ", Invoice_No_Start = " + Invoice_No_Start
                + ", OrderSMSCustomer = " + OrderSMSCustomer
                + ", OrdersmstimeDelay = " + OrdersmstimeDelay
                + ", PaymentEmailCustomer = " + PaymentEmailCustomer
                + ", OrdermailDaysDelay = " + OrdermailDaysDelay
                + ", Mail_Template = " + Mail_Template
                + ", MailTemplateIdCredit = " + MailTemplateIdCredit
                + ", Receipt_No_Start = " + Receipt_No_Start
                + ", OrderSMSTime = " + OrderSMSTime
                + ", OrdermailtimeDelay = " + OrdermailtimeDelay
                + ", PaymentmailDaysDelay = " + PaymentmailDaysDelay
                + ", SmsTemplateIdPayment = " + SmsTemplateIdPayment
                + ", SettingID = " + SettingID + ", CreatedDate = "
                + CreatedDate + ", TinNo = " + TinNo + ", CreditSMSCustomer = "
                + CreditSMSCustomer + ", OrderDefaultTaxApplicable = "
                + OrderDefaultTaxApplicable + ", MailTemplateIdPayment = "
                + MailTemplateIdPayment + ", AfterDueDatePeriod = "
                + AfterDueDatePeriod + ", AfterDueDateTime = "
                + AfterDueDateTime + ", CreditMailTime = " + CreditMailTime
                + "]";
    }
}