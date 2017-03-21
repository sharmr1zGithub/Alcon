package com.murho.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MDbConstant {
  public MDbConstant() {
  }


   /////////////////
   //invmst
    public final static String LOGIN_USER              = "CRBY";
    public final static String CREATED_AT              = "CRAT";
    public final static String UPDATED_BY              = "UPBY";
    public final static String UPDATED_AT              = "UPAT";
    
      
    public final static String SUCCESS_COLOR           = "maingreen";
    public final static String FAILED_COLOR            = "mainred";
    
    //recvHdr
    public final static String PLANT           = "PLANT";
    public final static String LOC             = "LOC";
    public final static String LOC1            = "LOC";
    public final static String TRAVELER        = "Traveler";
    public final static String TRAVELER_NUM    = "Traveler";
    public final static String PALLET          = "Pallet";
    public final static String MTID            = "MTID";
    public final static String SKU             = "SKU";
    public final static String ITEM            = "SKU";
    public final static String LOT             = "LOT";
    public final static String LOT_NUM         = "LOT";
    public final static String QTY             = "QTY";
    public final static String ORD_QTY         = "OrdQty";
    public final static String RECV_QTY        = "RecvQty";
    public final static String PUTAWAY_QTY     = "PutAwayQty";
    public final static String ReceiveStatus   = "ReceiveStatus";
    public final static String PutAwayStatus   = "PutAwayStatus";
    public final static String STATUS          = "STATUS";
    public final static String REMARK          = "REMARK";
    
    public final static String FRMLOC             = "FRMLOC";
    public final static String TOLOC              = "TOLOC";
    public final static String TEMPLOC            = "TEMPLOC";
    public final static String TRANSTYPE          = "TRANSTYPE";
    
    public final static String CRTIME             = "CRTIME";
    public final static String UPDATETIME         = "UPTIME";
    
    public final static String REFNO = "REFNO";
    public final static String TRAYPRINTLOTLENGTH="7";
    
    public final static String SHIPMARKTYPE="SHIP_TO";
   
    
    

   // public final static String MATERIAL_DESCRIPTION     = "COMMENT1";
  
    //recvDet
   
   ////////////////
   
  
   //
   public final static String COMPANY                  = "PLANT";
  
   public final static String MATERIAL_CODE            = "ITEM";
   public final static String JOB_NUM                  = "WONO";
   public final static String WHSE_ID                  = "WHID";
   public final static String BIN_NO                   = "BINNO";
   public final static String TRAN_QTY                 = "QTY";
   public final static String UOM                      = "USERFLD1";
  // public final static String TRAN_TYPE                = "LOC";
  // public final static String LOT_NUM                  = "BATCH";

  
   //Podet
   public final static String PO_NO                      = "PONO";
   public final static String PO_LN_NO                   = "POLNNO";
   public final static String VENDER_NO                  = "VENDNO";
   public final static String PURCHASE_POINT             = "PURPOINT";
   public final static String PACK_SLIP_NO               = "PACKSLIP";
   public final static String PACK_LN_NO                 = "PCKLINE";
   public final static String PODET_BINNO                = "LOC";
   public final static String PO_LN_STATUS               = "LNSTAT";
   public final static String PO_ITEM                    = "ITEM";
   public final static String PO_UOM                     = "UNITMO";
   public final static String JOB_NO                     = "USERFLD1";
   public final static String OP_SEQ                     = "USERFLD2";
   public final static String PO_BIN_NO                  = "LOC";


 

    //MOVHIS
     public final static String MOVHIS_PLANT           = "LOC";
     public final static String MOVHIS_REF_NUM         = "MOVTID";
     public final static String MOVHIS_LOT_NUM         = "BATNO";
     public final static String MOVHIS_WHID            = "WHID";
     public final static String MOVHIS_BIN_NUM         = "DEFBIN";
     public final static String MOVHIS_PO_NUM          = "PONO";
     public final static String MOVHIS_PO_LN_NUM       = "LNNO";
     public final static String MOVHIS_PACK_SLIP_NUM   = "USERFLD1";
     public final static String MOVHIS_UOM             = "USERFLD6";
     public final static String MOVHIS_REF_ID          = "MOVTID";
     public final static String MOVHIS_QTY             = "QTY";
     public final static String MOVHIS_DIRTYPE         = "DIRTYPE";
     public final static String MOVEHIS_CR_DATE        = "CRAT";
     public final static String MOVEHIS_CR_TIME        = "CRTIME";
     
    
      //itemMst
       public final static String ITEMMST_ITEM              =  "ITEM";
       public final static String ITEMMST_DESC              =  "ITEMDESC";
       public final static String ITEMMST_UOM               =  "STKUOM";
       public final static String ITEMMST_COST_METHOD       =  "COSTMETHOD";
       
//     Added By Ranjana Sharma on 22/05/2015 WO0000000471852 -UDI Part 2 Implementation
      public final static String ITEMMST_GTIN2				=  "GTIN2";

      //RecvHis
        
   
    //rsnmst
    
       public final static String RSNCODE              =  "RSNCODE";
       public final static String RSNDESC              =  "RSNDESC";
      
         
      
     
   //INVMST 
     public final static String INV_LOC                        ="LOC";

   // LOC_GROUP_MST
     public static final String LOC_GRP_ID                            ="LOC_GRP_ID";
     public static final String LOC_GRP_DESC                          ="LOC_GRP_DESC";
     public static final String TOTAL_BAY                             ="TOTAL_BAY";
     // LOC_MST
     public static final String LOC_ID                            ="LOC_ID";
     public static final String LOC_ID1                           ="LOC_ID";
     public static final String LOC_DESC                          ="LOC_DESC";
     public static final String SPACE_REM                            ="SPACE_REM";
     
     //PROD_GROUP_MST
     public static final String PRD_GRP_ID                          ="PRD_GRP_ID";
     public static final String PRD_GRP_DESC                        ="PRD_GRP_DESC";
     public static final String TRAYS_PER_BAY                        ="TRAYS_PER_BAY";
     public static final String PCNT_SPACE_PER_TRAY                 ="PCNT_SPACE_PER_TRAY";
    
    
     // PRD_CLASS_MST
     public static final String PRD_CLS_ID ="PRD_CLS_ID";
     public static final String PRD_CLS_DESC ="PRD_CLS_DESC";
     public static final String TRAY_GRP_ID ="TRAY_GRP_ID";
      public static final String PRD_CLS_UOM ="UOM";
       public static final String PRICE ="PRICE";
        public static final String KFACTOR ="KFACTOR";
         public static final String PRD_CLS_DESC1 ="PRD_CLS_DESC1";
      public static final String QTY_PER_COL ="QTY_PER_COLUMN";
      public static final String NO_OF_COLUMN ="NO_OF_COLUMN";
      public static final String QTY_PER_TRAY ="QTY_PER_TRAY";
      public static final String NO_OF_TRAY_PER_LAYER ="NO_OF_TRAY_PER_LAYER";
       public static final String NO_OF_LAYERS ="NO_OF_LAYERS";
       public static final String NO_OF_LAYERS_PER_PALLET ="NO_OF_LAYERS_PER_PALLET";
      
     //LOC_ASSINGING_RULE
     public static final String RULE_ID                          ="RulesId";
     public static final String RULE_DESC                        ="Description";
   //  public static final String PRD_GRP                          ="ProdGrpId";
   //  public static final String ASSIGN_LOC                       ="LocId";
   
    //DESTN_MST
     public static final String DESTINATION                       ="DESTINATION";
     public static final String SHIP_TO                           ="SHIP_TO";
     public static final String TRAV_PFX                          ="TRAV_PFX";
     
     //PALLET_GRP_MST
     public static final String PALLET_GRP                       ="PALLET_GRP";
       
        //TRAY_GRP_MST
    public static final String TRAY_GRP                          ="TRAY_GRP";
     public static final String TRAY_DESC                        ="TRAY_DESC";
       //RULE_MST
     public static final String PREFIX                           ="PREFIX";
     public static final String TYPE                             ="TYPE";

   //Tray_Det
   
   public final static String TrayDetStatus                     = "Status";
   public final static String TRAVELER_ID                       = "TRAVELER_ID";
    public static final String ASSIGNEDUSER                     ="ASSIGNEDUSER";
    

   
//TBLCONTOL
     public final static String TBL_FUNCTION   = "FUNC";
  public final static String TBL_PREFIX     = "PREFIX";
  public final static String TBL_MIN_SEQ    = "MINSEQ";
  public final static String TBL_MAX_SEQ    = "MAXSEQ";
  public final static String TBL_NEXT_SEQ   = "NXTSEQ";
  public final static String TBL_POSTFIX    = "POSTFIX";
  public final static String DESCRIPTION    = "DESCRIPTION";

 //CUSTMST
  
  
  public final static String CUST_TYPE     = "TYPE";
  public final static String CONTACT_NAME    = "CONTACT_NAME";
  public final static String COMPANY_NAME    = "COMPANY_NAME";
  public final static String ADDR1   = "ADDR1";
  public final static String ADDR2    = "ADDR2";
  public final static String ADDR3    = "ADDR3";
  public final static String COUNTRY   = "COUNTRY";
  public final static String ZIP     = "ZIP";
  public final static String PHONE    = "PHONE";
  public final static String FAX    = "FAX";
  
  //SHIP_DET
  
  public final static String INVOICE     = "INVOICE";
  public final static String NO_OF_TRAYS    = "NO_OF_TRAY";
  public final static String GROSS_WEIGHT    = "GROSS_WEIGHT";
  public final static String LENGTH   = "LENGTH";
  public final static String WIDTH    = "WIDTH";
  public final static String HEIGHT    = "HEIGHT";
  public final static String REMARKS1    = "REMARKS1";
  public final static String REMARKS2    = "REMARKS2";
      
  //LOT RESTRICTION
  public final static String REASON    = "REASON";
    
  //OB_TRAVEL_DET
  
 public final static String TRAYID     = "TRAYID";
 
 
 //Added for system blocking functionality under WMS version 5.0
//DESTLOCKBLOCK
 
 public final static String LOT_START_WITH     = "LOTSTARTWITH";
 public final static String DESTINATIONCODE    = "DESTINATIONCODE";
 
 public final static String COUNTRYNAME = "COUNTRYNAME";
}