import { apiFetch } from "@/services/httpClient";

export interface ProductType {
  id: string;
  name: string;
  description?: string;
  price: number;
  productId: string;
  createdAt: string;
  updatedAt: string;
  productName?: string;
}

export interface Product {
  id: string;
  name: string;
  description?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ProductTypePayload {
  name: string;
  description?: string;
  price: number;
  productId: string;
}

export interface ProductPayload {
  name: string;
  description?: string;
  active: boolean;
}

export const productService = {
  // Product Type operations
  async listProductTypes(): Promise<ProductType[]> {
    const response = await apiFetch("/api/branchreport/product-types");
    return response.json();
  },

  async getProductType(id: string): Promise<ProductType> {
    const response = await apiFetch(`/api/branchreport/product-types/${id}`);
    return response.json();
  },

  async createProductType(payload: ProductTypePayload): Promise<ProductType> {
    const response = await apiFetch(`/api/branchreport/product-types?productId=${payload.productId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: payload.name,
        description: payload.description,
        price: payload.price,
      }),
    });
    return response.json();
  },

  async updateProductType(id: string, payload: ProductTypePayload): Promise<ProductType> {
    const response = await apiFetch(`/api/branchreport/product-types/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    return response.json();
  },

  async deleteProductType(id: string): Promise<void> {
    await apiFetch(`/api/branchreport/product-types/${id}`, {
      method: "DELETE",
    });
  },

  // Product operations
  async listProducts(): Promise<Product[]> {
    const response = await apiFetch("/api/branchreport/products");
    return response.json();
  },

  async getProduct(id: string): Promise<Product> {
    const response = await apiFetch(`/api/branchreport/products/${id}`);
    return response.json();
  },

  async createProduct(payload: ProductPayload): Promise<Product> {
    const response = await apiFetch("/api/branchreport/products", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: payload.name,
        description: payload.description,
        active: payload.active,
      }),
    });
    return response.json();
  },

  async updateProduct(id: string, payload: ProductPayload): Promise<Product> {
    const response = await apiFetch(`/api/branchreport/products/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: payload.name,
        description: payload.description,
        active: payload.active,
      }),
    });
    return response.json();
  },

  async deleteProduct(id: string): Promise<void> {
    await apiFetch(`/api/branchreport/products/${id}`, {
      method: "DELETE",
    });
  },
};
