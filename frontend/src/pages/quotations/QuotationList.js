import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import Layout from '../../components/layout/Layout';
import { quotationService } from '../../services/quotationService';
import { FiPlus, FiEdit, FiTrash2, FiSend, FiCheckCircle } from 'react-icons/fi';

const QuotationList = () => {
  const [quotations, setQuotations] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadQuotations();
  }, []);

  const loadQuotations = async () => {
    try {
      const response = await quotationService.getAll();
      setQuotations(response.data);
    } catch (error) {
      console.error('Error loading quotations:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this quotation?')) {
      try {
        await quotationService.delete(id);
        loadQuotations();
      } catch (error) {
        console.error('Error deleting quotation:', error);
      }
    }
  };

  const getStatusBadge = (status) => {
    const statusColors = {
      DRAFT: 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300',
      PENDING_APPROVAL: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300',
      APPROVED: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300',
      SENT: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300',
      REJECTED: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300',
    };

    return (
      <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statusColors[status] || statusColors.DRAFT}`}>
        {status}
      </span>
    );
  };

  const canEdit = user?.role === 'SUPERADMIN' || user?.role === 'ADMIN';

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
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Quotations</h1>
            <p className="text-gray-600 dark:text-gray-400 mt-1">
              Manage all your quotations
            </p>
          </div>
          {canEdit && (
            <Link to="/quotations/create" className="btn btn-primary flex items-center space-x-2">
              <FiPlus size={20} />
              <span>Create Quotation</span>
            </Link>
          )}
        </div>

        {/* Quotations Table */}
        <div className="card overflow-hidden p-0">
          <div className="table-container">
            <table className="table">
              <thead className="table-header">
                <tr>
                  <th className="table-header-cell">Quotation #</th>
                  <th className="table-header-cell">Company</th>
                  <th className="table-header-cell">Total Amount</th>
                  <th className="table-header-cell">Status</th>
                  <th className="table-header-cell">Created At</th>
                  {canEdit && <th className="table-header-cell">Actions</th>}
                </tr>
              </thead>
              <tbody className="table-body">
                {quotations.length === 0 ? (
                  <tr>
                    <td colSpan={canEdit ? 6 : 5} className="table-cell text-center py-12">
                      <div className="text-gray-500 dark:text-gray-400">
                        <FiCheckCircle size={48} className="mx-auto mb-4 opacity-50" />
                        <p>No quotations found</p>
                        {canEdit && (
                          <Link to="/quotations/create" className="text-primary-600 hover:text-primary-700 mt-2 inline-block">
                            Create your first quotation
                          </Link>
                        )}
                      </div>
                    </td>
                  </tr>
                ) : (
                  quotations.map((quotation) => (
                    <tr key={quotation.id} className="hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors">
                      <td className="table-cell font-medium text-primary-600 dark:text-primary-400">
                        {quotation.quotationNumber}
                      </td>
                      <td className="table-cell">{quotation.companyId}</td>
                      <td className="table-cell font-semibold">
                        ${quotation.totalAmount?.toFixed(2) || '0.00'}
                      </td>
                      <td className="table-cell">{getStatusBadge(quotation.status)}</td>
                      <td className="table-cell">
                        {new Date(quotation.createdAt).toLocaleDateString()}
                      </td>
                      {canEdit && (
                        <td className="table-cell">
                          <div className="flex space-x-2">
                            <Link
                              to={`/quotations/edit/${quotation.id}`}
                              className="p-2 text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900 rounded transition-colors"
                              title="Edit"
                            >
                              <FiEdit size={18} />
                            </Link>
                            <button
                              onClick={() => handleDelete(quotation.id)}
                              className="p-2 text-red-600 hover:bg-red-50 dark:hover:bg-red-900 rounded transition-colors"
                              title="Delete"
                            >
                              <FiTrash2 size={18} />
                            </button>
                            {quotation.status === 'APPROVED' && (
                              <button
                                className="p-2 text-green-600 hover:bg-green-50 dark:hover:bg-green-900 rounded transition-colors"
                                title="Send to Client"
                              >
                                <FiSend size={18} />
                              </button>
                            )}
                          </div>
                        </td>
                      )}
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

export default QuotationList;
