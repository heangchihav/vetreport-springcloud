import { apiFetch } from "@/services/httpClient";

export interface SalesReport {
  id: string;
  productId: string;
  quantitySold: number;
  saleDate: string;
  branchId: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
  productName?: string;
  productTypeName?: string;
  price?: number;
  totalRevenue?: number;
}

export interface SalesSummary {
  productId: string;
  productName: string;
  productTypeName: string;
  totalQuantity: number;
  unitPrice: number;
  totalRevenue: number;
  firstSaleDate: string;
  lastSaleDate: string;
}

export interface DailySalesSummary {
  saleDate: string;
  totalTransactions: number;
  totalQuantity: number;
  totalRevenue: number;
}

export interface CreateSalesReportPayload {
  productId: string;
  quantitySold: number;
  saleDate: string;
  branchId: string;
  createdBy: string;
}

export interface UpdateSalesReportPayload {
  productId: string;
  quantitySold: number;
  saleDate: string;
  branchId: string;
}

export const salesService = {
  // Sales Report operations
  async listSalesReports(params?: {
    branchId?: string;
    startDate?: string;
    endDate?: string;
  }): Promise<SalesReport[]> {
    const searchParams = new URLSearchParams();
    if (params?.branchId) searchParams.append("branch_id", params.branchId);
    if (params?.startDate) searchParams.append("start_date", params.startDate);
    if (params?.endDate) searchParams.append("end_date", params.endDate);
    
    const url = `/api/branchreport/sales-reports${
      searchParams.toString() ? `?${searchParams.toString()}` : ""
    }`;
    const response = await apiFetch(url);
    return response.json();
  },

  async getSalesReport(id: string): Promise<SalesReport> {
    const response = await apiFetch(`/api/branchreport/sales-reports/${id}`);
    return response.json();
  },

  async createSalesReport(payload: CreateSalesReportPayload): Promise<SalesReport> {
    const response = await apiFetch("/api/branchreport/sales-reports", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        product_id: payload.productId,
        quantity_sold: payload.quantitySold,
        sale_date: payload.saleDate,
        branch_id: payload.branchId,
        created_by: payload.createdBy,
      }),
    });
    return response.json();
  },

  async updateSalesReport(id: string, payload: UpdateSalesReportPayload): Promise<SalesReport> {
    const response = await apiFetch(`/api/branchreport/sales-reports/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        product_id: payload.productId,
        quantity_sold: payload.quantitySold,
        sale_date: payload.saleDate,
        branch_id: payload.branchId,
      }),
    });
    return response.json();
  },

  async deleteSalesReport(id: string): Promise<void> {
    await apiFetch(`/api/branchreport/sales-reports/${id}`, {
      method: "DELETE",
    });
  },

  // Sales Summary operations
  async getSalesSummary(params?: {
    branchId?: string;
    startDate?: string;
    endDate?: string;
  }): Promise<SalesSummary[]> {
    const searchParams = new URLSearchParams();
    if (params?.branchId) searchParams.append("branch_id", params.branchId);
    if (params?.startDate) searchParams.append("start_date", params.startDate);
    if (params?.endDate) searchParams.append("end_date", params.endDate);
    
    const url = `/api/branchreport/sales-summary${
      searchParams.toString() ? `?${searchParams.toString()}` : ""
    }`;
    const response = await apiFetch(url);
    return response.json();
  },

  async getDailySalesSummary(params?: {
    branchId?: string;
    startDate?: string;
    endDate?: string;
  }): Promise<DailySalesSummary[]> {
    const searchParams = new URLSearchParams();
    if (params?.branchId) searchParams.append("branch_id", params.branchId);
    if (params?.startDate) searchParams.append("start_date", params.startDate);
    if (params?.endDate) searchParams.append("end_date", params.endDate);
    
    const url = `/api/branchreport/daily-sales-summary${
      searchParams.toString() ? `?${searchParams.toString()}` : ""
    }`;
    const response = await apiFetch(url);
    return response.json();
  },
};
