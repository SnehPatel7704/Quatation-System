import React, { useState, useEffect } from 'react';
import Layout from '../../components/layout/Layout';
import { productService } from '../../services/productService';
import { FiPlus, FiEdit, FiTrash2, FiPackage } from 'react-icons/fi';

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const response = await productService.getAll();
      setProducts(response.data);
    } catch (error) {
      console.error('Error loading products:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productService.delete(id);
        loadProducts();
      } catch (error) {
        console.error('Error deleting product:', error);
      }
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Products</h1>
            <p className="text-gray-600 dark:text-gray-400 mt-1">Manage product catalog</p>
          </div>
          <button className="btn btn-primary flex items-center space-x-2">
            <FiPlus size={20} />
            <span>Add Product</span>
          </button>
        </div>

        <div className="card overflow-hidden p-0">
          <div className="table-container">
            <table className="table">
              <thead className="table-header">
                <tr>
                  <th className="table-header-cell">Name</th>
                  <th className="table-header-cell">Description</th>
                  <th className="table-header-cell">Base Price</th>
                  <th className="table-header-cell">Actions</th>
                </tr>
              </thead>
              <tbody className="table-body">
                {products.length === 0 ? (
                  <tr>
                    <td colSpan={4} className="table-cell text-center py-12">
                      <FiPackage size={48} className="mx-auto mb-4 opacity-50 text-gray-400" />
                      <p className="text-gray-500 dark:text-gray-400">No products found</p>
                    </td>
                  </tr>
                ) : (
                  products.map((product) => (
                    <tr key={product.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                      <td className="table-cell font-medium">{product.name}</td>
                      <td className="table-cell">{product.description}</td>
                      <td className="table-cell font-semibold">
                        ${product.basePrice?.toFixed(2) || '0.00'}
                      </td>
                      <td className="table-cell">
                        <div className="flex space-x-2">
                          <button className="p-2 text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900 rounded">
                            <FiEdit size={18} />
                          </button>
                          <button
                            onClick={() => handleDelete(product.id)}
                            className="p-2 text-red-600 hover:bg-red-50 dark:hover:bg-red-900 rounded"
                          >
                            <FiTrash2 size={18} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default ProductManagement;
