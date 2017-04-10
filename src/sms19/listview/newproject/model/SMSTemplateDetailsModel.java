package sms19.listview.newproject.model;

import java.util.ArrayList;

/**
 * Created by android on 23/2/17.
 */

public class SMSTemplateDetailsModel {
    private String Status;

    private String Message;

    private ArrayList<SMSTemplateDetails> Details;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public ArrayList<SMSTemplateDetails> getDetails ()
    {
        return Details;
    }

    public void setDetails (ArrayList<SMSTemplateDetails> Details)
    {
        this.Details = Details;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Message = "+Message+", Details = "+Details+"]";
    }

    public class SMSTemplateDetails
    {
        private String template;

        private String template_title;

        private String template_id;

        private String user_id;

        private String m_id;

        public String getTemplate ()
        {
            return template;
        }

        public void setTemplate (String template)
        {
            this.template = template;
        }

        public String getTemplate_title ()
        {
            return template_title;
        }

        public void setTemplate_title (String template_title)
        {
            this.template_title = template_title;
        }

        public String getTemplate_id ()
        {
            return template_id;
        }

        public void setTemplate_id (String template_id)
        {
            this.template_id = template_id;
        }

        public String getUser_id ()
        {
            return user_id;
        }

        public void setUser_id (String user_id)
        {
            this.user_id = user_id;
        }

        public String getM_id ()
        {
            return m_id;
        }

        public void setM_id (String m_id)
        {
            this.m_id = m_id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [template = "+template+", template_title = "+template_title+", template_id = "+template_id+", user_id = "+user_id+", m_id = "+m_id+"]";
        }
    }
}
