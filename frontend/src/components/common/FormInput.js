import React from 'react';
import PropTypes from 'prop-types';

/**
 * Reusable form input component with label, validation, and error display
 * 
 * @param {string} label - The label text for the input
 * @param {string} name - The name attribute for the input
 * @param {string} type - The input type (text, email, password, number, date, etc.)
 * @param {string|number} value - The current value of the input
 * @param {function} onChange - Handler function for value changes
 * @param {string} error - Error message to display (if any)
 * @param {boolean} required - Whether the field is required
 * @param {string} placeholder - Placeholder text
 * @param {boolean} disabled - Whether the input is disabled
 * @param {React.ReactNode} icon - Optional icon to display before the label
 * @param {string} className - Additional CSS classes for the input
 * @param {object} inputProps - Additional props to pass to the input element
 */
const FormInput = ({
  label,
  name,
  type = 'text',
  value,
  onChange,
  error,
  required = false,
  placeholder = '',
  disabled = false,
  icon = null,
  className = '',
  ...inputProps
}) => {
  const inputId = `input-${name}`;
  const hasError = Boolean(error);

  return (
    <div className="w-full">
      {label && (
        <label htmlFor={inputId} className="label">
          {icon && <span className="inline mr-2">{icon}</span>}
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}
      
      <input
        id={inputId}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        className={`input ${hasError ? 'border-red-500 focus:ring-red-500' : ''} ${className}`}
        aria-invalid={hasError}
        aria-describedby={hasError ? `${inputId}-error` : undefined}
        {...inputProps}
      />
      
      {hasError && (
        <p
          id={`${inputId}-error`}
          className="mt-1 text-sm text-red-600 dark:text-red-400"
          role="alert"
        >
          {error}
        </p>
      )}
    </div>
  );
};

FormInput.propTypes = {
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
  type: PropTypes.string,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func.isRequired,
  error: PropTypes.string,
  required: PropTypes.bool,
  placeholder: PropTypes.string,
  disabled: PropTypes.bool,
  icon: PropTypes.node,
  className: PropTypes.string,
};

export default FormInput;
