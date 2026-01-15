import api from './api';

export const userService = {
  getAll: () => api.get('/superadmin/users'),
  
  create: (userData) => api.post('/superadmin/users', userData),
  
  update: (id, userData) => api.put(`/superadmin/users/${id}`, userData),
  
  delete: (id) => api.delete(`/superadmin/users/${id}`),
};
