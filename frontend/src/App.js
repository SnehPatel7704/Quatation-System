import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ThemeProvider } from './contexts/ThemeContext';
import PrivateRoute from './components/common/PrivateRoute';
import Login from './pages/auth/Login';
import Dashboard from './pages/dashboard/Dashboard';
import QuotationList from './pages/quotations/QuotationList';
import QuotationCreate from './pages/quotations/QuotationCreate';
import QuotationEdit from './pages/quotations/QuotationEdit';
import UserManagement from './pages/users/UserManagement';
import CompanyManagement from './pages/companies/CompanyManagement';
import ProductManagement from './pages/products/ProductManagement';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            
            <Route path="/dashboard" element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            } />
            
            <Route path="/quotations" element={
              <PrivateRoute>
                <QuotationList />
              </PrivateRoute>
            } />
            
            <Route path="/quotations/create" element={
              <PrivateRoute roles={['SUPERADMIN', 'ADMIN']}>
                <QuotationCreate />
              </PrivateRoute>
            } />
            
            <Route path="/quotations/edit/:id" element={
              <PrivateRoute roles={['SUPERADMIN', 'ADMIN']}>
                <QuotationEdit />
              </PrivateRoute>
            } />
            
            <Route path="/users" element={
              <PrivateRoute roles={['SUPERADMIN']}>
                <UserManagement />
              </PrivateRoute>
            } />
            
            <Route path="/companies" element={
              <PrivateRoute roles={['SUPERADMIN', 'ADMIN']}>
                <CompanyManagement />
              </PrivateRoute>
            } />
            
            <Route path="/products" element={
              <PrivateRoute roles={['SUPERADMIN', 'ADMIN']}>
                <ProductManagement />
              </PrivateRoute>
            } />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
