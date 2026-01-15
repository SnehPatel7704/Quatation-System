import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import Layout from '../../components/layout/Layout';
import { FiFileText, FiUsers, FiBriefcase, FiPackage, FiTrendingUp } from 'react-icons/fi';

const Dashboard = () => {
  const { user } = useAuth();

  const cards = [
    {
      title: 'Quotations',
      icon: FiFileText,
      link: '/quotations',
      color: 'bg-blue-500',
      description: 'Manage all quotations',
      show: true,
    },
    {
      title: 'Companies',
      icon: FiBriefcase,
      link: '/companies',
      color: 'bg-green-500',
      description: 'Manage companies',
      show: user?.role === 'SUPERADMIN' || user?.role === 'ADMIN',
    },
    {
      title: 'Products',
      icon: FiPackage,
      link: '/products',
      color: 'bg-purple-500',
      description: 'Manage products',
      show: user?.role === 'SUPERADMIN' || user?.role === 'ADMIN',
    },
    {
      title: 'Users',
      icon: FiUsers,
      link: '/users',
      color: 'bg-red-500',
      description: 'Manage system users',
      show: user?.role === 'SUPERADMIN',
    },
  ];

  return (
    <Layout>
      <div className="space-y-8">
        {/* Welcome Section */}
        <div className="card">
          <div className="flex items-center space-x-4">
            <div className="flex-shrink-0">
              <div className="w-16 h-16 bg-primary-100 dark:bg-primary-900 rounded-full flex items-center justify-center">
                <FiTrendingUp size={32} className="text-primary-600 dark:text-primary-400" />
              </div>
            </div>
            <div>
              <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                Welcome back, {user?.username}!
              </h1>
              <p className="text-gray-600 dark:text-gray-400 mt-1">
                Role: <span className="font-semibold text-primary-600 dark:text-primary-400">{user?.role}</span>
              </p>
            </div>
          </div>
        </div>

        {/* Role-based Access Info */}
        <div className="card bg-gradient-to-r from-primary-50 to-blue-50 dark:from-gray-800 dark:to-gray-700 border-l-4 border-primary-600">
          <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-3">
            Your Access Level
          </h2>
          {user?.role === 'SUPERADMIN' && (
            <div className="space-y-2 text-gray-700 dark:text-gray-300">
              <p className="flex items-center">
                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                Full system access including user management
              </p>
              <p className="flex items-center">
                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                Create, edit, approve, and delete quotations
              </p>
              <p className="flex items-center">
                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                Manage companies, products, and users
              </p>
            </div>
          )}
          {user?.role === 'ADMIN' && (
            <div className="space-y-2 text-gray-700 dark:text-gray-300">
              <p className="flex items-center">
                <span className="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                Create, edit, and approve quotations
              </p>
              <p className="flex items-center">
                <span className="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                Manage companies and products
              </p>
              <p className="flex items-center">
                <span className="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                Send quotations to clients
              </p>
            </div>
          )}
          {user?.role === 'USER' && (
            <div className="space-y-2 text-gray-700 dark:text-gray-300">
              <p className="flex items-center">
                <span className="w-2 h-2 bg-yellow-500 rounded-full mr-2"></span>
                View quotations
              </p>
              <p className="flex items-center">
                <span className="w-2 h-2 bg-yellow-500 rounded-full mr-2"></span>
                View companies and products
              </p>
            </div>
          )}
        </div>

        {/* Quick Access Cards */}
        <div>
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-6">
            Quick Access
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {cards.filter(card => card.show).map((card, index) => (
              <Link
                key={index}
                to={card.link}
                className="group card hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
              >
                <div className="flex flex-col items-center text-center space-y-4">
                  <div className={`${card.color} w-16 h-16 rounded-full flex items-center justify-center group-hover:scale-110 transition-transform duration-300`}>
                    <card.icon size={32} className="text-white" />
                  </div>
                  <div>
                    <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
                      {card.title}
                    </h3>
                    <p className="text-sm text-gray-600 dark:text-gray-400">
                      {card.description}
                    </p>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        </div>

        {/* Recent Activity Placeholder */}
        <div className="card">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
            Recent Activity
          </h2>
          <div className="text-center py-12 text-gray-500 dark:text-gray-400">
            <FiFileText size={48} className="mx-auto mb-4 opacity-50" />
            <p>No recent activity to display</p>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;
