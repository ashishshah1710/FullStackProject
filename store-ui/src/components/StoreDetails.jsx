import React, { useState } from 'react';
import { getStore } from '../api/storeService';

function StoreDetails() {
  const [storeId, setStoreId] = useState('');
  const [store, setStore] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleFetch = async () => {
    if (!storeId.trim()) {
      setError('Please enter a store ID');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await getStore(storeId);
      setStore(response.data);
    } catch (error) {
      setError('Error fetching store details. Please try again.');
      setStore(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="component-title">
        <i className="fas fa-search icon"></i>
        Store Details
      </h2>
      
      <div className="input-group">
        <input 
          className="form-input"
          value={storeId} 
          onChange={(e) => setStoreId(e.target.value)} 
          placeholder="🔍 Enter Store ID" 
        />
        <button 
          onClick={handleFetch} 
          disabled={loading}
          className="btn btn-secondary"
        >
          {loading ? (
            <>
              <span className="loading-spinner"></span>
              Fetching...
            </>
          ) : (
            <>
              <i className="fas fa-download icon"></i>
              Fetch
            </>
          )}
        </button>
      </div>
      
      {error && (
        <div className="error-message">
          <i className="fas fa-exclamation-triangle icon"></i>
          {error}
        </div>
      )}
      
      {store && (
        <div className="store-details-display">
          <h3>
            <i className="fas fa-info-circle icon"></i>
            Store Information
          </h3>
          <p><strong>🆔 ID:</strong> {store.id}</p>
          <p><strong>🏪 Store Name:</strong> {store.storeName}</p>
          <p><strong>📍 Address:</strong> {store.address}</p>
          <p><strong>👤 Manager:</strong> {store.managerName}</p>
        </div>
      )}
    </div>
  );
}

export default StoreDetails;