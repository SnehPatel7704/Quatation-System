import React from 'react';
import { useParams } from 'react-router-dom';
import Layout from '../../components/layout/Layout';

const QuotationEdit = () => {
  const { id } = useParams();

  return (
    <Layout>
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-6">
          Edit Quotation #{id}
        </h1>
        <div className="card">
          <p className="text-gray-600 dark:text-gray-400">Edit form coming soon...</p>
        </div>
      </div>
    </Layout>
  );
};

export default QuotationEdit;
