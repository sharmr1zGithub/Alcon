package com.murho.utils;

public class TransactionTypes 
{
  public TransactionTypes()
  {
  }

  //Inbound
  public static final String Import_inbound_file_tran_type="IDEL_FILE_IMPORT";
  public static final String Import_inbound_file_delete_tran_type="IDEL_FILE_DELETE";
  public static final String Import_inbound_mtid_reject_tran_type="IDEL_MTID_REJECT";
  public static final String Import_inbound_mtid_update_tran_type="IDEL_MTID_UPDATE";
  
  
  //OutBound
  public static final String Import_outbound_file_tran_type="ODEL_FILE_IMPORT";
  
}