package com.example.srcismeterjuly;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
	List<XMLParserObject> CISData;
    private XMLParserObject cis_ref;
    private String text,text2;
    
    public XMLParser() {
    	CISData = new ArrayList<XMLParserObject>();
    }
    
    public List<XMLParserObject> getCISData() {
        return CISData;
    }

    public List<XMLParserObject> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                case XmlPullParser.START_TAG:
                    // if <table> create a new instance of post
                    if (tagname.equalsIgnoreCase("BFRead")){                    	 
                        cis_ref = new XMLParserObject();
                        text2="DATA";
                        cis_ref.setprecode(text2);
                    }else if (tagname.equalsIgnoreCase("Constant")){                    	 
                 	   	cis_ref = new XMLParserObject();
                   }else if (tagname.equalsIgnoreCase("Dbst51")){                    	 
                	   	cis_ref = new XMLParserObject();
                	   	text2 = "D51";
                	   	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("Dbst52")){                    	 
                    	cis_ref= new XMLParserObject();
                    	text2 = "D52";
                	   	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("Dbst53")){                    	 
                    	cis_ref = new XMLParserObject();
                    	text2 = "D53";
                	   	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("Dbst54")){                    	 
                    	cis_ref = new XMLParserObject();
                    	text2 = "D54";
                	   	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("Dbst06")){                    	 
                    	cis_ref = new XMLParserObject();
                    	text2="D06";
                    	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("Dbst42")){
                    	cis_ref = new XMLParserObject();
                    	text2="D42";
                    	cis_ref.setwwcode(text2);
                    }else if (tagname.equalsIgnoreCase("msEmployee")){
                    	cis_ref = new XMLParserObject();
                    	text2="EMP";
                    	cis_ref.setprecode(text2);
                    }else if (tagname.equalsIgnoreCase("PData")){
                        cis_ref = new XMLParserObject();
                        text2="PDATA";
                        cis_ref.setprecode(text2);
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("BFRead")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst51")){                   	
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst52")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst53")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst54")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst06")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("Dbst42")){
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("msEmployee")) {
                        CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("PData")){
                        CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("wwcode")){
                        cis_ref.setwwcode(text);
                    }else if (tagname.equalsIgnoreCase("mtrrdroute")){
                    	cis_ref.setmtrrdroute(text);
                    }else if (tagname.equalsIgnoreCase("mtrseq")){
                    	cis_ref.setmtrseq(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("custcode")){
                    	cis_ref.setcustcode(text);
                    }else if (tagname.equalsIgnoreCase("usertype")){
                    	cis_ref.setusertype(text);
                    }else if (tagname.equalsIgnoreCase("oldtype")){
                    	cis_ref.setoldtype(text);
                    }else if (tagname.equalsIgnoreCase("custname")){
                    	cis_ref.setcustname(text);
                    }else if (tagname.equalsIgnoreCase("custaddr")){
                    	cis_ref.setcustaddr(text);
                    }else if (tagname.equalsIgnoreCase("location")){
                    	cis_ref.setlocation(text);
                    }else if (tagname.equalsIgnoreCase("mtrmkcode")){
                    	cis_ref.setmtrmkcode(text);
                    }else if (tagname.equalsIgnoreCase("metersize")){
                    	cis_ref.setmetersize(text);
                    }else if (tagname.equalsIgnoreCase("meterno")){
                    	cis_ref.setmeterno(text);
                    }else if (tagname.equalsIgnoreCase("prsmtrstat")){
                    	cis_ref.setprsmtrstat(text);
                    }else if (tagname.equalsIgnoreCase("lstmtrddt")){
                    	cis_ref.setlstmtrddt(text);
                    }else if (tagname.equalsIgnoreCase("lstmtrcnt")){
                    	cis_ref.setlstmtrcnt(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("revym")){
                    	cis_ref.setrevym(text);
                    }else if (tagname.equalsIgnoreCase("novat")){
                    	cis_ref.setnovat(text);
                    }else if (tagname.equalsIgnoreCase("avgwtuse")){
                    	cis_ref.setavgwtuse(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("discntcode")){
                    	cis_ref.setdiscntcode(text);
                    }else if (tagname.equalsIgnoreCase("invoicecnt")){
                    	cis_ref.setinvoicecnt(text);
                    }else if (tagname.equalsIgnoreCase("invflag")){
                    	cis_ref.setinvflag(text);
                    }else if (tagname.equalsIgnoreCase("debmonth")){
                    	cis_ref.setdebmonth(Integer.parseInt(text));
                   }else if (tagname.equalsIgnoreCase("debamt")){
                	   cis_ref.setdebamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("remwtusg")){
                    	cis_ref.setremwtusg(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("noofhouse")){
                    	cis_ref.setnoofhouse(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("pwa_flag")){
                    	cis_ref.setpwaflag(text);
                    }else if (tagname.equalsIgnoreCase("meterest")){
                    	cis_ref.setmeterest(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("smcnt")){
                    	cis_ref.setsmcnt(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("mincharge")){
                    	cis_ref.setmincharge(text);
                    }else if (tagname.equalsIgnoreCase("lstwtusg")){
                    	cis_ref.setlstwtusg(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("subdiscnt")){
                    	cis_ref.setsubdiscn(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("readflag")){
                    	cis_ref.setreadflag(text);
                    }else if (tagname.equalsIgnoreCase("newread")){
                    	cis_ref.setnewread(text);
                    }else if (tagname.equalsIgnoreCase("prsmtrcnt")){
                    	cis_ref.setprsmtrcnt(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("nortrfwt")){
                    	cis_ref.setnortrfwt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("nortrfwt")){
                    	cis_ref.setnortrfwt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("discntamt")){
                    	cis_ref.setdiscntamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("srvfee")){
                    	cis_ref.setsrvfee(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("vat")){
                    	cis_ref.setvat(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("tottrfwt")){
                    	cis_ref.settottrfwt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("comment")){
                    	cis_ref.setcomment(text);
                    }else if (tagname.equalsIgnoreCase("commentdec")){
                    	cis_ref.setcommentdec(text);
                    }else if (tagname.equalsIgnoreCase("billflag")){
                    	cis_ref.setbillflag(text);
                    }else if (tagname.equalsIgnoreCase("billsend")){
                    	cis_ref.setbillsend(text);
                    }else if (tagname.equalsIgnoreCase("hln")){
                    	cis_ref.sethln(text);
                    }else if (tagname.equalsIgnoreCase("prswtusg")){
                    	cis_ref.setprswtusg(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("latitude")){
                    	cis_ref.setlatitude(text);
                    }else if (tagname.equalsIgnoreCase("lontitude")){
                    	cis_ref.setlontitude(text);
                    }else if (tagname.equalsIgnoreCase("prsmtrrddt")){
                    	cis_ref.setprsmtrrddt(text);
                    }else if (tagname.equalsIgnoreCase("time")){
                    	cis_ref.settime(text);
                    }else if (tagname.equalsIgnoreCase("readcount")){
                    	cis_ref.setreadcount(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("printcount")){
                    	cis_ref.setprintcount(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("usgcalmthd")){
                    	cis_ref.setusgcalmthd(text);
                    }else if (tagname.equalsIgnoreCase("userid")){
                    	cis_ref.setuserid(text);  	
                    }else if (tagname.equalsIgnoreCase("chkdigit")){
                    	cis_ref.setchkdigit(text);
                    }else if (tagname.equalsIgnoreCase("controlmtr")){
                    	cis_ref.setcontrolmtr(text);
                    }else if (tagname.equalsIgnoreCase("bigmtrno")){
                    	cis_ref.setbigmtrno(text);
                    }else if (tagname.equalsIgnoreCase("tbseq")){
                    	cis_ref.settbset(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("dupamt")){
                    	cis_ref.setdupamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("service_flag")){
                    	cis_ref.setserviceflag(text);	
                    }else if (tagname.equalsIgnoreCase("corporate_no")){
                    	cis_ref.setregisno(text);
                    }else if (tagname.equalsIgnoreCase("branch_order")){
                    	cis_ref.setcustbrn(text);
                    }else if (tagname.equalsIgnoreCase("mon1")){
                        cis_ref.setmon1(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("mon2")){
                        cis_ref.setmon2(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("mon3")){
                        cis_ref.setmon3(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("Constant")) {
                    	CISData.add(cis_ref);
                    }else if (tagname.equalsIgnoreCase("wwnamet")){
                    	cis_ref.setprecode("CON");                    	
                    	cis_ref.setwwnamet(text);
                    }else if (tagname.equalsIgnoreCase("wwtel")){
                    	cis_ref.setwwtel(text);
                    }else if (tagname.equalsIgnoreCase("ba")){
                    	cis_ref.setba(text);
                    }else if (tagname.equalsIgnoreCase("mvamt1")){
                    	cis_ref.setmvamt1(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("mvamt2")){
                    	cis_ref.setmvamt2(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("mvamt3")){
                    	cis_ref.setmvamt3(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("dispwapro")) {
                        cis_ref.setdispwapro(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("tax_code")) {
                        cis_ref.settaxcode(text);
                    }else if (tagname.equalsIgnoreCase("branch_order_con")) {
                        cis_ref.setbranchorder2(text);
                    }else if (tagname.equalsIgnoreCase("org_addr")){
                        cis_ref.setorgaddr(text);
                    }else if(tagname.equalsIgnoreCase("wttrfrt")){     
                 	   cis_ref.setwttrfrt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("acwttrfamt")){
                 	   cis_ref.setacwttrfamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("lowusgran")){         
                 	   cis_ref.setlowusgran(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("highusgran")){                   
                 	   cis_ref.sethighusgran(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("discntmean")){
                    	cis_ref.setdiscntmean(text);
                    }else if (tagname.equalsIgnoreCase("discnttype")){
                    	cis_ref.setdiscnttype(text);
                    }else if (tagname.equalsIgnoreCase("discntsrvf")){
                    	cis_ref.setdiscntsrvf(text);
                    }else if (tagname.equalsIgnoreCase("discntpcen")){
                    	cis_ref.setdiscntpcen(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("discntbaht")){
                    	cis_ref.setdiscntbaht(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("discntnumr")){
                    	cis_ref.setdiscntnumr(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("discntdnom")){
                    	cis_ref.setdiscntdnom(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("discntunit")){
                    	cis_ref.setdiscntunit(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("metersize")){
                    	cis_ref.setmetersize(text);
                    }else if (tagname.equalsIgnoreCase("srvfee")){
                    	cis_ref.setsrvfee(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("empid")){
                    	cis_ref.setempid(text);
                    }else if (tagname.equalsIgnoreCase("empname")){
                    	cis_ref.setempname(text);
                    }else if (tagname.equalsIgnoreCase("emplastname")){
                    	cis_ref.setemplastname(text);
                    }else if (tagname.equalsIgnoreCase("empmobile")){
                    	cis_ref.setempmoblie(text);
                    }else if (tagname.equalsIgnoreCase("empposition")){
                    	cis_ref.setempposition(text);
                    }else if (tagname.equalsIgnoreCase("wwcode_p")){
                        cis_ref.setprecode("PDATA");
                        cis_ref.setwwcoded(text);
                    }else if (tagname.equalsIgnoreCase("custcode_p")){
                        cis_ref.setcustcoded(text);
                    }else if (tagname.equalsIgnoreCase("custname_p")){
                        cis_ref.setcustnamed(text);
                    }else if (tagname.equalsIgnoreCase("custaddr_p")){
                        cis_ref.setcustaddrd(text);
                    }else if (tagname.equalsIgnoreCase("corporate_no_p")){
                        cis_ref.setcorporateno(text);
                    }else if (tagname.equalsIgnoreCase("branch_order_p")){
                        cis_ref.setbranchorderd(text);
                    }else if (tagname.equalsIgnoreCase("water_bill_no")){
                        cis_ref.setwaterbillno(text);
                    }else if (tagname.equalsIgnoreCase("paid_date")){
                        cis_ref.setpaiddate(text);
                    }else if (tagname.equalsIgnoreCase("invoice_no")){
                        cis_ref.setinvoiceno(text);
                    }else if (tagname.equalsIgnoreCase("present_meter_date")){
                        cis_ref.setpresentmeterdate(text);
                    }else if (tagname.equalsIgnoreCase("present_meter_count")){
                        cis_ref.setpresentmetercount(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("present_meter_usg")){
                        cis_ref.setpresentmeterusg(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("debt_ym")){
                        cis_ref.setdebtym(text);
                    }else if (tagname.equalsIgnoreCase("normal_water_amt")){
                        cis_ref.setnormalwateramt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("discount_amt")){
                        cis_ref.setdiscountamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("net_water_amt")){
                        cis_ref.setnetwateramt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("service_amt")){
                        cis_ref.setserviceamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("net_water_amt2")){
                        cis_ref.setnetwateramt2(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("vat_amt")){
                        cis_ref.setvatamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("total_water_amt")){
                        cis_ref.settotalwateramt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("duplicate_amt")){
                        cis_ref.setduplicateamt(Double.parseDouble(text));
                    }else if (tagname.equalsIgnoreCase("paid_channel")){
                        cis_ref.setpaidchannel(text);
                    }else if (tagname.equalsIgnoreCase("channel_name")){
                        cis_ref.setchannelname(text);
                    }else if (tagname.equalsIgnoreCase("bank_acc")){
                        cis_ref.setbankacc(text);
                    }else if (tagname.equalsIgnoreCase("account_page")){
                        cis_ref.setaccountpage(text);
                    }else if (tagname.equalsIgnoreCase("receiver_code")){
                        cis_ref.setreceivercode(text);
                    }else if (tagname.equalsIgnoreCase("receiver")){
                        cis_ref.setreceiver(text);
                    }else if (tagname.equalsIgnoreCase("org_rev")){
                        cis_ref.setorgrev(text);
                    }else if (tagname.equalsIgnoreCase("flag")){
                        cis_ref.setflag(text);
                    }
                    break;
                default:
                    break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CISData;
    }
}
