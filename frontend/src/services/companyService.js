import api from './api';

export const companyService = {
  getAll: () => api.get('/companies'),
  
  getById: (id) => api.get(`/companies/${id}`),
  
  create: (companyData) => api.post('/companies', companyData),
  
  update: (id, companyData) => api.put(`/companies/${id}`, companyData),
  
  delete: (id) => api.delete(`/companies/${id}`),
};
