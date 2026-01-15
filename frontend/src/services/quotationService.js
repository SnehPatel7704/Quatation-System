import api from './api';

export const quotationService = {
  getAll: () => api.get('/quotations'),
  
  getById: (id) => api.get(`/quotations/${id}`),
  
  create: (quotationData) => api.post('/quotations', quotationData),
  
  update: (id, quotationData) => api.put(`/quotations/${id}`, quotationData),
  
  delete: (id) => api.delete(`/quotations/${id}`),
  
  approve: (id, adminEmail, clientEmail) => 
    api.post(`/quotations/${id}/approve`, null, {
      params: { adminEmail, clientEmail }
    }),
  
  sendToClient: (id, clientEmail) => 
    api.post(`/quotations/${id}/send`, null, {
      params: { clientEmail }
    }),
};
