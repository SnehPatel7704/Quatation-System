import api from './api';

export const productService = {
  getAll: () => api.get('/products'),
  
  getById: (id) => api.get(`/products/${id}`),
  
  create: (productData) => api.post('/products', productData),
  
  update: (id, productData) => api.put(`/products/${id}`, productData),
  
  delete: (id) => api.delete(`/products/${id}`),
};
