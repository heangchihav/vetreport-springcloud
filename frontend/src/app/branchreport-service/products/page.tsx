"use client";

import { useEffect, useState } from "react";
import { BranchreportServiceGuard } from "@/components/branchreport-service/BranchreportServiceGuard";
import { useToast } from "@/components/ui/Toast";
import { productService, ProductType, Product, ProductTypePayload, ProductPayload } from "@/services/branchreport-service/productService";

type FormState<T> = Partial<T>;

const defaultProductTypeForm: FormState<ProductTypePayload> = {
  name: "",
  description: "",
  price: 0,
  productId: "",
};

const defaultProductForm: FormState<ProductPayload> = {
  name: "",
  description: "",
  active: true,
};

export default function ProductManagementPage() {
  const { showToast } = useToast();

  // Product Types State
  const [productTypes, setProductTypes] = useState<ProductType[]>([]);
  const [productTypeForm, setProductTypeForm] = useState<FormState<ProductTypePayload>>(defaultProductTypeForm);
  const [editingProductType, setEditingProductType] = useState<string | null>(null);

  // Products State
  const [products, setProducts] = useState<Product[]>([]);
  const [productForm, setProductForm] = useState<FormState<ProductPayload>>(defaultProductForm);
  const [editingProduct, setEditingProduct] = useState<string | null>(null);

  // Loading states
  const [loadingProductTypes, setLoadingProductTypes] = useState(false);
  const [loadingProducts, setLoadingProducts] = useState(false);
  const [submittingProductType, setSubmittingProductType] = useState(false);
  const [submittingProduct, setSubmittingProduct] = useState(false);

  // Load data
  useEffect(() => {
    loadProductTypes();
    loadProducts();
  }, []);

  const loadProductTypes = async () => {
    try {
      setLoadingProductTypes(true);
      const data = await productService.listProductTypes();
      setProductTypes(data);
    } catch (error) {
      showToast("Failed to load product types", "error");
      console.error("Error loading product types:", error);
    } finally {
      setLoadingProductTypes(false);
    }
  };

  const loadProducts = async () => {
    try {
      setLoadingProducts(true);
      const data = await productService.listProducts();
      setProducts(data);
    } catch (error) {
      showToast("Failed to load products", "error");
      console.error("Error loading products:", error);
    } finally {
      setLoadingProducts(false);
    }
  };

  // Product Type handlers
  const handleCreateOrUpdateProductType = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!productTypeForm.name || !productTypeForm.price) {
      showToast("Name and price are required", "error");
      return;
    }

    try {
      setSubmittingProductType(true);

      if (editingProductType) {
        await productService.updateProductType(editingProductType, productTypeForm as ProductTypePayload);
        showToast("Product type updated successfully", "success");
      } else {
        await productService.createProductType(productTypeForm as ProductTypePayload);
        showToast("Product type created successfully", "success");
      }

      setProductTypeForm(defaultProductTypeForm);
      setEditingProductType(null);
      loadProductTypes();
    } catch (error) {
      showToast(`Failed to ${editingProductType ? "update" : "create"} product type`, "error");
      console.error("Error:", error);
    } finally {
      setSubmittingProductType(false);
    }
  };

  const handleEditProductType = (productType: ProductType) => {
    setProductTypeForm({
      name: productType.name,
      description: productType.description,
      price: productType.price,
    });
    setEditingProductType(productType.id);
  };

  const handleDeleteProductType = async (id: string) => {
    if (!confirm("Are you sure you want to delete this product type? This will also delete all products of this type.")) {
      return;
    }

    try {
      await productService.deleteProductType(id);
      showToast("Product type deleted successfully", "success");
      loadProductTypes();
      loadProducts(); // Reload products to remove deleted items
    } catch (error) {
      showToast("Failed to delete product type", "error");
      console.error("Error:", error);
    }
  };

  const handleCancelProductTypeEdit = () => {
    setProductTypeForm(defaultProductTypeForm);
    setEditingProductType(null);
  };

  // Product handlers
  const handleCreateOrUpdateProduct = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!productForm.name) {
      showToast("Name is required", "error");
      return;
    }

    try {
      setSubmittingProduct(true);

      if (editingProduct) {
        await productService.updateProduct(editingProduct, productForm as ProductPayload);
        showToast("Product updated successfully", "success");
      } else {
        await productService.createProduct(productForm as ProductPayload);
        showToast("Product created successfully", "success");
      }

      setProductForm(defaultProductForm);
      setEditingProduct(null);
      loadProducts();
    } catch (error) {
      showToast(`Failed to ${editingProduct ? "update" : "create"} product`, "error");
      console.error("Error:", error);
    } finally {
      setSubmittingProduct(false);
    }
  };

  const handleEditProduct = (product: Product) => {
    setProductForm({
      name: product.name,
      description: product.description,
      active: product.active,
    });
    setEditingProduct(product.id);
  };

  const handleDeleteProduct = async (id: string) => {
    if (!confirm("Are you sure you want to delete this product?")) {
      return;
    }

    try {
      await productService.deleteProduct(id);
      showToast("Product deleted successfully", "success");
      loadProducts();
    } catch (error) {
      showToast("Failed to delete product", "error");
      console.error("Error:", error);
    }
  };

  const handleCancelProductEdit = () => {
    setProductForm(defaultProductForm);
    setEditingProduct(null);
  };

  return (
    <BranchreportServiceGuard>
      <div className="min-h-screen">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold text-slate-100 mb-8">Product Management</h1>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Products Section */}
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg shadow-xl p-6 border border-slate-600/50">
              <h2 className="text-xl font-semibold text-slate-100 mb-4">
                {editingProduct ? "Edit Product" : "Create Product"}
              </h2>

              <form onSubmit={handleCreateOrUpdateProduct} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Name *
                  </label>
                  <input
                    type="text"
                    value={productForm.name || ""}
                    onChange={(e) => setProductForm({ ...productForm, name: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                    placeholder="Enter product name"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Description
                  </label>
                  <textarea
                    value={productForm.description || ""}
                    onChange={(e) => setProductForm({ ...productForm, description: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                    placeholder="Enter description"
                    rows={3}
                  />
                </div>

                <div>
                  <label className="flex items-center text-sm font-medium text-slate-200">
                    <input
                      type="checkbox"
                      checked={productForm.active ?? true}
                      onChange={(e) => setProductForm({ ...productForm, active: e.target.checked })}
                      className="mr-2 rounded border-slate-600 bg-slate-700 text-green-600 focus:ring-green-500"
                    />
                    Active
                  </label>
                </div>

                <div className="flex space-x-2">
                  <button
                    type="submit"
                    disabled={submittingProduct}
                    className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  >
                    {submittingProduct ? "Saving..." : (editingProduct ? "Update" : "Create")}
                  </button>
                  {editingProduct && (
                    <button
                      type="button"
                      onClick={handleCancelProductEdit}
                      className="flex-1 bg-slate-600 text-slate-100 py-2 px-4 rounded-md hover:bg-slate-700 transition-colors"
                    >
                      Cancel
                    </button>
                  )}
                </div>
              </form>

              {/* Products List */}
              <div className="mt-6">
                <h3 className="text-lg font-medium text-slate-100 mb-3">Products</h3>
                {loadingProducts ? (
                  <div className="text-center py-4 text-slate-400">Loading...</div>
                ) : (
                  <div className="space-y-2 max-h-64 overflow-y-auto">
                    {products.map((product) => (
                      <div key={product.id} className="flex items-center justify-between p-3 bg-slate-700/50 rounded-md border border-slate-600/30">
                        <div>
                          <div className="font-medium text-slate-100">{product.name}</div>
                          {product.description && (
                            <div className="text-xs text-slate-500">{product.description}</div>
                          )}
                        </div>
                        <div className="flex space-x-2">
                          <button
                            onClick={() => handleEditProduct(product)}
                            className="text-blue-400 hover:text-blue-300 text-sm transition-colors"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDeleteProduct(product.id)}
                            className="text-red-400 hover:text-red-300 text-sm transition-colors"
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>

            {/* Product Types Section */}
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg shadow-xl p-6 border border-slate-600/50">
              <h2 className="text-xl font-semibold text-slate-100 mb-4">
                {editingProductType ? "Edit Product Type" : "Create Product Type"}
              </h2>

              <form onSubmit={handleCreateOrUpdateProductType} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Name *
                  </label>
                  <input
                    type="text"
                    value={productTypeForm.name || ""}
                    onChange={(e) => setProductTypeForm({ ...productTypeForm, name: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-slate-100 placeholder-slate-400"
                    placeholder="Enter product type name"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Description
                  </label>
                  <textarea
                    value={productTypeForm.description || ""}
                    onChange={(e) => setProductTypeForm({ ...productTypeForm, description: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-slate-100 placeholder-slate-400"
                    placeholder="Enter description"
                    rows={3}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Price ($) *
                  </label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={productTypeForm.price || ""}
                    onChange={(e) => setProductTypeForm({ ...productTypeForm, price: parseFloat(e.target.value) || 0 })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-slate-100 placeholder-slate-400"
                    placeholder="0.00"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-200 mb-1">
                    Product *
                  </label>
                  <select
                    value={productTypeForm.productId || ""}
                    onChange={(e) => setProductTypeForm({ ...productTypeForm, productId: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-slate-100"
                    required
                  >
                    <option value="">Select a product</option>
                    {products.map((product) => (
                      <option key={product.id} value={product.id}>
                        {product.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="flex space-x-2">
                  <button
                    type="submit"
                    disabled={submittingProductType}
                    className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  >
                    {submittingProductType ? "Saving..." : (editingProductType ? "Update" : "Create")}
                  </button>
                  {editingProductType && (
                    <button
                      type="button"
                      onClick={handleCancelProductTypeEdit}
                      className="flex-1 bg-slate-600 text-slate-100 py-2 px-4 rounded-md hover:bg-slate-700 transition-colors"
                    >
                      Cancel
                    </button>
                  )}
                </div>
              </form>

              {/* Product Types List */}
              <div className="mt-6">
                <h3 className="text-lg font-medium text-slate-100 mb-3">Product Types</h3>
                {loadingProductTypes ? (
                  <div className="text-center py-4 text-slate-400">Loading...</div>
                ) : (
                  <div className="space-y-2 max-h-64 overflow-y-auto">
                    {productTypes.map((productType) => (
                      <div key={productType.id} className="flex items-center justify-between p-3 bg-slate-700/50 rounded-md border border-slate-600/30">
                        <div>
                          <div className="font-medium text-slate-100">{productType.name}</div>
                          <div className="text-sm text-slate-400">${productType.price.toFixed(2)}</div>
                          {productType.description && (
                            <div className="text-xs text-slate-500">{productType.description}</div>
                          )}
                        </div>
                        <div className="flex space-x-2">
                          <button
                            onClick={() => handleEditProductType(productType)}
                            className="text-blue-400 hover:text-blue-300 text-sm transition-colors"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDeleteProductType(productType.id)}
                            className="text-red-400 hover:text-red-300 text-sm transition-colors"
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </BranchreportServiceGuard>
  );
}
