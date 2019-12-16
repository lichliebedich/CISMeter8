package com.example.srcismeterjuly;

public class XMLParserObject {

    private String precode;
    private String wwcode;
    private String mtrrdroute;
    private int mtrseq;
    private String custcode;
    private String usertype;
    private String oldtype;
    private String custstat;
    private String location;
    private String custname;
    private String custaddr;
    private String mtrmkcode;
    private String metersize;
    private String meterno;
    private String prsmtrstat;
    private String lstmtrddt;
    private int lstmtrcnt;
    private String revym;
    private String novat;
    private int avgwtuse;
    private String discntcode;
    private String invoicecnt;
    private String invflag;
    private int debmonth;
    private double debamt;
    private int remwtusg;
    private int noofhouse;
    private String pwa_flag;
    private int meterest;
    private int smcnt;
    private String mincharge;
    private int lstwtusg;
    private int subdiscn;
    private String readflag;
    private String newread;
    private int prsmtrcnt;
    private double nortrfwt;
    private double discntamt;
    private double srvfee;
    private double vat;
    private double tottrfwt;
    private String comment;
    private String CommentDec;
    private String billflag;
    private String billsend;
    private String hln;
    private int prswtusg;
    private String latitude;
    private String lontitude;
    private String prsmtrrddt;
    private String time;
    private int readcount;
    private int printcount;
    private String usgcalmthd;
    private String userid;
    private String chkdigit;
    private String controlmtr;
    private String bigmtrno;
    private int tbseq;
    private String service_flag;
    private String regisno;
    private String custbrn;
    private double dupamt;
    private double mon1;
    private double mon2;
    private double mon3;

    //Constant
    //private String wwcode;
    private String wwnamet;
    private String wwtel;
    private String ba;
    private double mvamt1;
    private double mvamt2;
    private double mvamt3;
    private double dispwapro;

    //dbst51,52,53,53
    private double wttrfrt;
    private double acwttrfamt;
    private int lowusgran;
    private int highusgran;

    //dbst06
    private String discntmean,discnttype,discntsrvf;
    private double discntpcen,discntbaht;
    private int discntnumr,discntdnom,discntunit;

    //msEmployee
    private String empid,empname,emplastname,empmoblie,empposition;

    //addfilde29forbank2019
    private String wwcoded;
    private String custcoded;
    private String custnamed;
    private String custaddrd;
    private String corporateno;
    private String branchorder;
    private String waterbillno;
    private String paiddate;
    private String invoiceno;
    private String presentmeterdate;
    private int presentmetercount;
    private int presentmeterusg;
    private String debtym;
    private double normalwateramt;
    private double discountamt;
    private double netwateramt;
    private double serviceamt;
    private double netwateramt2;
    private double vatamt;
    private double duplicateamt;
    private String paidchannel;
    private String bankacc;
    private String receiver;
    private String orgrev;
    private String flag;
    private String receivercode;
    private double totalwateramt;
    private String channelname;
    private String accountpage;
    private String taxcode;
    private String orgaddr;
    private String branchorder2;
    private String branchorderd;


    public String getprecode(){
        return precode;
    }

    public void setprecode(String precode){
        this.precode=precode;
    }

    public String getwwcode(){
        return wwcode;
    }

    public void setwwcode(String wwcode){
        this.wwcode=wwcode;
    }

    public String getwwnamet(){
        return wwnamet;
    }

    public void setwwnamet(String wwnamet){
        this.wwnamet=wwnamet;
    }


    public String getwwtel(){
        return wwtel;
    }

    public void setwwtel(String wwtel){
        this.wwtel=wwtel;
    }

    public String getba(){
        return ba;
    }

    public void setba(String ba){
        this.ba=ba;
    }

    public double getmvamt1(){
        return mvamt1;
    }
    public void setmvamt1(double mvamt1){
        this.mvamt1 = mvamt1;
    }
    public double getmvamt2(){
        return mvamt2;
    }
    public void setmvamt2(double mvamt2){
        this.mvamt2 = mvamt2;
    }
    public double getmvamt3(){
        return mvamt3;
    }
    public void setmvamt3(double mvamt3){
        this.mvamt3 = mvamt3;
    }
    public double getdispwapro(){
        return dispwapro;
    }
    public void setdispwapro(double dispwapro){
        this.dispwapro= dispwapro;
    }


    public String getmtrrdroute(){
        return mtrrdroute;
    }

    public void setmtrrdroute(String mtrrdroute){
        this.mtrrdroute = mtrrdroute;
    }

    public int getmtrseq(){
        return mtrseq;
    }

    public void setmtrseq(int mtrseq){
        this.mtrseq=mtrseq;
    }

    public String getcustcode(){
        return custcode;
    }

    public void setcustcode(String custcode){
        this.custcode= custcode;
    }

    public String getusertype(){
        return usertype;
    }

    public void setusertype(String usertype){
        this.usertype = usertype;
    }

    public String getoldtype(){
        return oldtype;
    }

    public void setoldtype(String oldtype){
        this.oldtype = oldtype;
    }

    public String getcuststat(){
        return custstat;
    }

    public void setcuststat(String custstat){
        this.custstat = custstat;
    }

    public String getlocation(){
        return location;
    }

    public void setlocation(String location){
        this.location = location;
    }

    public String getcustname(){
        return custname;
    }

    public void setcustname(String custname){
        this.custname = custname;
    }

    public String getcustaddr(){
        return custaddr;
    }
    public void setcustaddr(String custaddr){
        this.custaddr = custaddr;
    }

    public String getmtrmkcode(){
        return mtrmkcode;
    }
    public void setmtrmkcode(String mtrmkcode){
        this.mtrmkcode = mtrmkcode;
    }
    public String getmetersize(){
        return metersize;
    }
    public void setmetersize(String metersize){
        this.metersize = metersize;
    }
    public String getmeterno(){
        return meterno;
    }
    public void setmeterno(String meterno){
        this.meterno = meterno;
    }
    public String getprsmtrstat(){
        return prsmtrstat;
    }
    public void setprsmtrstat(String prsmtrstat){
        this.prsmtrstat = prsmtrstat;
    }
    public String getlstmtrddt(){
        return lstmtrddt;
    }
    public void setlstmtrddt(String lstmtrddt){
        this.lstmtrddt = lstmtrddt;
    }
    public int getlstmtrcnt(){
        return lstmtrcnt;
    }
    public void setlstmtrcnt(int lstmtrcnt){
        this.lstmtrcnt = lstmtrcnt;
    }
    public String getrevym(){
        return revym;
    }
    public void setrevym(String revym){
        this.revym = revym;
    }
    public String getnovat(){
        return novat;
    }
    public void setnovat(String novat){
        this.novat = novat;
    }
    public int getavgwtuse(){
        return avgwtuse;
    }
    public void setavgwtuse(int avgwtuse){
        this.avgwtuse = avgwtuse;
    }
    public String getdiscntcode(){
        return discntcode;
    }
    public void setdiscntcode(String discntcode){
        this.discntcode = discntcode;
    }
    public String getinvoicecnt(){
        return invoicecnt;
    }
    public void setinvoicecnt(String invoicecnt){
        this.invoicecnt = invoicecnt;
    }
    public String getinvflag(){
        return invflag;
    }
    public void setinvflag(String invflag){
        this.invflag = invflag;
    }
    public int getdebmonth(){
        return debmonth;
    }
    public void setdebmonth(int debmonth){
        this.debmonth = debmonth;
    }
    public double getdebamt(){
        return debamt;
    }
    public void setdebamt(double debamt){
        this.debamt = debamt;
    }
    public int getremwtusg(){
        return remwtusg;
    }
    public void setremwtusg(int remwtusg){
        this.remwtusg = remwtusg;
    }
    public int getnoofhouse(){
        return noofhouse;
    }
    public void setnoofhouse(int noofhouse){
        this.noofhouse = noofhouse;
    }
    public String getpwaflag(){
        return pwa_flag;
    }
    public void setpwaflag(String pwa_flag){
        this.pwa_flag = pwa_flag;
    }
    public int getmeterest(){
        return meterest;
    }
    public void setmeterest(int meterest){
        this.meterest = meterest;
    }
    public int getsmcnt(){
        return smcnt;
    }
    public void setsmcnt(int smcnt){
        this.smcnt = smcnt;
    }
    public String getmincharge(){
        return mincharge;
    }
    public void setmincharge(String mincharge){
        this.mincharge = mincharge;
    }
    public int getlstwtusg(){
        return lstwtusg;
    }
    public void setlstwtusg(int lstwtusg){
        this.lstwtusg = lstwtusg;
    }
    public int getsubdiscn(){
        return subdiscn;
    }
    public void setsubdiscn(int subdiscn){
        this.subdiscn = subdiscn;
    }
    public String getreadflag(){
        return readflag;
    }
    public void setreadflag(String readflag){
        this.readflag = readflag;
    }
    public String getnewread(){
        return newread;
    }
    public void setnewread(String newread){
        this.newread = newread;
    }
    public int getprsmtrcnt(){
        return prsmtrcnt;
    }
    public void setprsmtrcnt(int prsmtrcnt){
        this.prsmtrcnt = prsmtrcnt;
    }
    public double getnortrfwt(){
        return nortrfwt;
    }
    public void setnortrfwt(double nortrfwt){
        this.nortrfwt = nortrfwt;
    }
    public double getdiscntamt(){
        return discntamt;
    }
    public void setdiscntamt(double discntamt){
        this.discntamt = discntamt;
    }
    public double getsrvfee(){
        return srvfee;
    }
    public void setsrvfee(double srvfee){
        this.srvfee = srvfee;
    }
    public double getvat(){
        return vat;
    }
    public void setvat(double vat){
        this.vat = vat;
    }
    public double gettottrfwt(){
        return tottrfwt;
    }
    public void settottrfwt(double tottrfwt){
        this.tottrfwt = tottrfwt;
    }
    public String getcomment(){
        return comment;
    }
    public void setcomment(String comment){
        this.comment = comment;
    }
    public String getcommentdec(){
        return CommentDec;
    }
    public void setcommentdec(String commentdec){
        this.CommentDec = commentdec;
    }
    public String getbillflag(){
        return billflag;
    }
    public void setbillflag(String billflag){
        this.billflag = billflag;
    }
    public String getbillsend(){
        return billsend;
    }
    public void setbillsend(String billsend){
        this.billsend = billsend;
    }
    public String gethln(){
        return hln;
    }
    public void sethln(String hln){
        this.hln = hln;
    }
    public int getprswtusg(){
        return prswtusg;
    }
    public void setprswtusg(int prswtusg){
        this.prswtusg = prswtusg;
    }
    public String getlatitude(){
        return latitude;
    }
    public void setlatitude(String latitude){
        this.latitude = latitude;
    }
    public String getlontitude(){
        return lontitude;
    }
    public void setlontitude(String lontitude){
        this.lontitude = lontitude;
    }
    public String getprsmtrrddt(){
        return prsmtrrddt;
    }
    public void setprsmtrrddt(String prsmtrrddt){
        this.prsmtrrddt = prsmtrrddt;
    }
    public String gettime(){
        return time;
    }
    public void settime(String time){
        this.time = time;
    }
    public int getreadcount(){
        return readcount;
    }
    public void setreadcount(int readcount){
        this.readcount = readcount;
    }
    public int getprintcount(){
        return printcount;
    }
    public void setprintcount(int printcount){
        this.printcount = printcount;
    }
    public String getusgcalmthd(){
        return usgcalmthd;
    }
    public void setusgcalmthd(String usgcalmthd){
        this.usgcalmthd = usgcalmthd;
    }
    public String getuserid(){
        return userid;
    }
    public void setuserid(String userid){
        this.userid = userid;
    }
    public String getchkdigit(){
        return chkdigit;
    }
    public void setchkdigit(String chkdigit){
        this.chkdigit = chkdigit;
    }
    public String getcontrolmtr(){
        return controlmtr;
    }
    public void setcontrolmtr(String controlmtr){
        this.controlmtr=controlmtr;
    }
    public String getbigmtrno(){
        return bigmtrno;
    }
    public void setbigmtrno(String bigmtrno){
        this.bigmtrno=bigmtrno;
    }
    public int gettbseq(){
        return tbseq;
    }
    public void settbset(int tbseq){
        this.tbseq=tbseq;
    }
    public double getdupamt(){
        return dupamt;
    }
    public void setdupamt(double dupamt){
        this.dupamt=dupamt;
    }
    public String getserviceflag(){
        return service_flag;
    }
    public void setserviceflag(String service_flag){
        this.service_flag=service_flag;
    }
    public double getmon1(){
        return mon1;
    }
    public void setmon1(double mon1){
        this.mon1 = mon1;
    }
    public double getmon2(){
        return mon2;
    }
    public void setmon2(double mon2){
        this.mon2 = mon2;
    }
    public double getmon3(){
        return mon3;
    }
    public void setmon3(double mon3){
        this.mon3 = mon3;
    }


    public String getregisno(){
        return regisno;
    }
    public void setregisno(String regisno){
        this.regisno=regisno;
    }
    public String getcustbrn(){
        return custbrn;
    }
    public void setcustbrn(String custbrn){
        this.custbrn=custbrn;
    }

    public double getwttrfrt(){
        return wttrfrt;
    }
    public void setwttrfrt(double wttrfrt){
        this.wttrfrt = wttrfrt;
    }
    public double getacwttrfamt(){
        return acwttrfamt;
    }
    public void setacwttrfamt(double acwttrfamt){
        this.acwttrfamt = acwttrfamt;
    }
    public int getlowusgran(){
        return lowusgran;
    }
    public void setlowusgran(int lowusgran){
        this.lowusgran = lowusgran;
    }
    public int gethighusgran(){
        return highusgran;
    }
    public void sethighusgran(int highusgran){
        this.highusgran = highusgran;
    }

    public String getdiscntmean(){
        return discntmean;
    }
    public void setdiscntmean(String discntmean){
        this.discntmean = discntmean;
    }
    public String getdiscnttype(){
        return discnttype;
    }
    public void setdiscnttype(String discnttype){
        this.discnttype = discnttype;
    }
    public String getdiscntsrvf(){
        return discntsrvf;
    }
    public void setdiscntsrvf(String discntsrvf){
        this.discntsrvf = discntsrvf;
    }
    public double getdiscntpcen(){
        return discntpcen;
    }
    public void setdiscntpcen(double discntpcen){
        this.discntpcen = discntpcen;
    }
    public double getdiscntbaht(){
        return discntbaht;
    }
    public void setdiscntbaht(double discntbaht){
        this.discntbaht = discntbaht;
    }
    public int getdiscntnumr(){
        return discntnumr;
    }
    public void setdiscntnumr(int discntnumr){
        this.discntnumr = discntnumr;
    }
    public int getdiscntdnom(){
        return discntdnom;
    }
    public void setdiscntdnom(int discntdnom){
        this.discntdnom =discntdnom;
    }
    public int getdiscntunit(){
        return discntunit;
    }
    public void setdiscntunit(int discntunit){
        this.discntunit = discntunit;
    }

    public String getempid(){
        return empid;
    }
    public void setempid(String empid){
        this.empid=empid;
    }
    public String getempname(){
        return empname;
    }
    public void setempname(String empname){
        this.empname=empname;
    }
    public String getemplastname(){
        return emplastname;
    }
    public void setemplastname(String emplastname){
        this.emplastname=emplastname;
    }
    public String getempmoblie(){
        return empmoblie;
    }
    public void setempmoblie(String empmoblie){
        this.empmoblie=empmoblie;
    }
    public String getempposition(){
        return empposition;
    }
    public void setempposition(String empposition){
        this.empposition=empposition;
    }

    public void setcorporateno(String corporateno) {
        this.corporateno = corporateno;
    }
    public String getcorporateno() {
        return corporateno;
    }
    public void setcustcoded(String custcoded) {
        this.custcoded = custcoded;
    }
    public String getcustcoded() {
        return custcoded;
    }
    public void setcustnamed(String custnamed) {
        this.custnamed = custnamed;
    }
    public String getcustnamed() {
        return custnamed;
    }
    public void setcustaddrd(String custaddrd) {
        this.custaddrd = custaddrd;
    }
    public String getcustaddrd() {
        return custaddrd;
    }

    public void setbranchorderd(String branchorder) {
        this.branchorder = branchorder;
    }

    public String getbranchorder() {
        return branchorder;
    }

    public void setwaterbillno(String waterbillno) {
        this.waterbillno = waterbillno;
    }

    public String getwaterbillno() {
        return waterbillno;
    }

    public void setpaiddate(String paiddate) {
        this.paiddate = paiddate;
    }

    public String getpaiddate() {
        return paiddate;
    }

    public void setinvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getinvoiceno() {
        return invoiceno;
    }

    public void setpresentmeterdate(String presentmeterdate) {
        this.presentmeterdate = presentmeterdate;
    }

    public String getPresentmeterdate() {
        return presentmeterdate;
    }

    public void setpresentmetercount(int presentmetercount) {
        this.presentmetercount = presentmetercount;
    }

    public int getpresentmetercount() {
        return presentmetercount;
    }

    public void setpresentmeterusg(int presentmeterusg) {
        this.presentmeterusg = presentmeterusg;
    }

    public int getpresentmeterusg() {
        return presentmeterusg;
    }

    public void setdebtym(String debtym) {
        this.debtym = debtym;
    }

    public String getdebtym() {
        return debtym;
    }

    public void setnormalwateramt(double normalwateramt) {
        this.normalwateramt = normalwateramt;
    }

    public double getnormalwateramt() {
        return normalwateramt;
    }

    public void setdiscountamt(double discountamt) {
        this.discountamt = discountamt;
    }

    public double getdiscountamt() {
        return discountamt;
    }

    public void setnetwateramt(double netwateramt) {
        this.netwateramt = netwateramt;
    }

    public double getnetwateramt() {
        return netwateramt;
    }


    public void setserviceamt(double serviceamt) {
        this.serviceamt = serviceamt;
    }

    public double getserviceamt() {
        return serviceamt;
    }

    public void setnetwateramt2(double netwateramt2) {
        this.netwateramt2 = netwateramt2;
    }

    public double getnetwateramt2() {
        return netwateramt2;
    }

    public void setvatamt(double vatamt) {
        this.vatamt = vatamt;
    }

    public double getvatamt() {
        return vatamt;
    }

    public void settotalwateramt(double totalwateramt) {
        this.totalwateramt = totalwateramt;
    }

    public double gettotalwateramt() {
        return totalwateramt;
    }

    public void setduplicateamt(double duplicateamt) {
        this.duplicateamt = duplicateamt;
    }

    public double getduplicateamt() {
        return duplicateamt;
    }

    public void setpaidchannel(String paidchannel) {
        this.paidchannel = paidchannel;
    }

    public String getpaidchannel() {
        return paidchannel;
    }

    public void setchannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getchannelname() {
        return channelname;
    }

    public void setbankacc(String bankacc) {
        this.bankacc = bankacc;
    }

    public String getbankacc() {
        return bankacc;
    }

    public void setaccountpage(String accountpage) {
        this.accountpage = accountpage;
    }

    public String getaccountpage() {
        return accountpage;
    }

    public void setreceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getreceiver() {
        return receiver;
    }

    public void setorgrev(String orgrev) {
        this.orgrev = orgrev;
    }

    public String getorgrev() {
        return orgrev;
    }

    public void setflag(String flag) {
        this.flag = flag;
    }

    public String getflag() {
        return flag;
    }


    public void setreceivercode(String receivercode) {
        this.receivercode = receivercode;
    }

    public String getreceivercode() {
        return receivercode;
    }


    public void setwwcoded(String wwcoded) {
        this.wwcoded = wwcoded;
    }

    public String getwwcoded() {
        return wwcoded;
    }


    public void settaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String gettaxcode() {
        return taxcode;
    }

    public void setorgaddr(String orgaddr) {
        this.orgaddr = orgaddr;
    }

    public String getorgaddr() {
        return orgaddr;
    }

    public void setbranchorder2(String branchorder2) {
        this.branchorder2 = branchorder2;
    }

    public String getbranchorder2() {
        return branchorder2;
    }

    //
    @Override
    public String toString() {
        //return "เส้นทาง : " + mtrrdroute +","+ custcode +revym + custcode + custaddr +"\n";
        return "เส้นทาง : "+ wwcode + " "+ wwnamet + " "+ discntmean + " " + wttrfrt + " "
                + acwttrfamt+ " " + empid + " " + empname + " " + srvfee + "\n";
    }




}
