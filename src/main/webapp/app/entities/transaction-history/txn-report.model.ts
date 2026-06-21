/** Aggregated transaction report returned by GET api/transaction-report (mirrors the backend TxnReportDto). */
export interface ITxnReport {
  discountVoucherCount: number;
  receivedVoucherCount: number;
  vatVoucherCount: number;
  refundVoucherCount: number;
  totalVoucherCount: number;

  totalDiscount: number;
  totalReceived: number;
  totalVat: number;
  totalRefund: number;
  totalPayableAmount: number;
  due: number;

  xRayAmount: number;
  hallMarkAmount: number;
  normalMeltingAmount: number;
  normalMeltingServiceChargeAmount: number;
  calculatedMeltingAmount: number;
  calculatedMeltingServiceChargeAmount: number;
}
