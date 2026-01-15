import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Layout from '../../components/layout/Layout';
import { quotationService } from '../../services/quotationService';
import { FiSave, FiX } from 'react-icons/fi';

const QuotationCreate = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    companyId: '',
    items: [{ productId: '', quantity: 1, unitPrice: 0 }],
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await quotationService.create({
        quotation: { companyId: formData.companyId, createdBy: 1 },
        items: formData.items,
      });
      navigate('/quotations');
    } catch (error) {
      console.error('Error creating quotation:', error);
    }
  };

  return (
    <Layout>
      <div className="max-w-4xl mx-auto space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Create Quotation</h1>
        </div>

        <form onSubmit={handleSubmit} className="card space-y-6">
          <div>
            <label className="label">Company ID</label>
            <input
              type="number"
              value={formData.companyId}
              onChange={(e) => setFormData({ ...formData, companyId: e.target.value })}
              className="input"
              required
            />
          </div>

          <div className="flex space-x-4">
            <button type="submit" className="btn btn-primary flex items-center space-x-2">
              <FiSave size={18} />
              <span>Create Quotation</span>
            </button>
            <button
              type="button"
              onClick={() => navigate('/quotations')}
              className="btn btn-secondary flex items-center space-x-2"
            >
              <FiX size={18} />
              <span>Cancel</span>
            </button>
          </div>
        </form>
      </div>
    </Layout>
  );
};

export default QuotationCreate;
