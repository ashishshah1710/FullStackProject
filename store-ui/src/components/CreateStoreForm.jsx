import React, { useState } from 'react';
import { createStore } from '../api/storeService';

function CreateStoreForm() {
  const [storeData, setStoreData] = useState({ 
    storeName: '', 
    address: '', 
    managerName: '' 
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setStoreData({ ...storeData, [e.target.name]: e.target.value });
    if (success) setSuccess(false); // Clear success message when user starts typing
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const dataToSend = {
        storeName: storeData.storeName,
        address: storeData.address,
        managerName: storeData.managerName
      };
      
      const response = await createStore(dataToSend);
      setSuccess(true);
      setStoreData({ storeName: '', address: '', managerName: '' });
      
      // Auto-hide success message after 3 seconds
      setTimeout(() => setSuccess(false), 3000);
    } catch (error) {
      console.error('Error creating store:', error);
      alert('❌ Error creating store. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="component-title">
        <i className="fas fa-plus-circle icon"></i>
        Create New Store
      </h2>
      
      {success && (
        <div className="success-message">
          <i className="fas fa-check-circle icon"></i>
          🎉 Store created successfully! Ready to add another one?
        </div>
      )}
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <input 
            className="form-input"
            name="storeName" 
            placeholder="🏪 Enter Store Name (e.g., Downtown Electronics)" 
            value={storeData.storeName}
            onChange={handleChange}
            required 
          />
        </div>
        
        <div className="form-group">
          <input 
            className="form-input"
            name="address" 
            placeholder="📍 Enter Full Address (Street, City, State)" 
            value={storeData.address}
            onChange={handleChange}
            required 
          />
        </div>
        
        <div className="form-group">
          <input 
            className="form-input"
            name="managerName" 
            placeholder="👤 Enter Manager's Full Name" 
            value={storeData.managerName}
            onChange={handleChange}
            required 
          />
        </div>
        
        <div className="tooltip">
          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? (
              <>
                <span className="loading-spinner"></span>
                Creating Your Store...
              </>
            ) : (
              <>
                <i className="fas fa-rocket icon"></i>
                Create Amazing Store
              </>
            )}
          </button>
          <span className="tooltiptext">Click to create a new store with the provided information</span>
        </div>
      </form>
    </div>
  );
}

export default CreateStoreForm;
