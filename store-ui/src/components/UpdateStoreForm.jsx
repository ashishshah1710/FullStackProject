import React, { useState } from 'react';
import { updateStore, getStore } from '../api/storeService';

function UpdateStoreForm() {
  const [storeData, setStoreData] = useState({ 
    id: '', 
    storeName: '', 
    address: '', 
    managerName: '' 
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [fetchLoading, setFetchLoading] = useState(false);
  const [storeFetched, setStoreFetched] = useState(false);

  const handleChange = (e) => {
    setStoreData({ ...storeData, [e.target.name]: e.target.value });
    if (success) setSuccess(false);
    if (error) setError('');
  };

  const handleFetchStore = async () => {
    if (!storeData.id.trim()) {
      setError('Please enter a store ID first');
      return;
    }

    setFetchLoading(true);
    setError('');
    
    try {
      const response = await getStore(storeData.id);
      const store = response.data;
      
      setStoreData({
        id: store.id,
        storeName: store.storeName,
        address: store.address,
        managerName: store.managerName
      });
      setStoreFetched(true);
    } catch (error) {
      console.error('Error fetching store:', error);
      setError('❌ Store not found. Please check the ID and try again.');
      setStoreFetched(false);
    } finally {
      setFetchLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!storeFetched) {
      setError('Please fetch the store details first before updating');
      return;
    }
    
    setLoading(true);
    setError('');
    
    try {
      const dataToSend = {
        id: storeData.id,
        storeName: storeData.storeName,
        address: storeData.address,
        managerName: storeData.managerName
      };
      
      const response = await updateStore(dataToSend);
      setSuccess(true);
      
      // Auto-hide success message after 3 seconds
      setTimeout(() => setSuccess(false), 3000);
    } catch (error) {
      console.error('Error updating store:', error);
      setError('❌ Error updating store. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setStoreData({ id: '', storeName: '', address: '', managerName: '' });
    setStoreFetched(false);
    setSuccess(false);
    setError('');
  };

  return (
    <div>
      <h2 className="component-title">
        <i className="fas fa-edit icon"></i>
        Update Store
      </h2>
      
      {success && (
        <div className="success-message">
          <i className="fas fa-check-circle icon"></i>
          🎉 Store updated successfully! All changes have been saved.
        </div>
      )}

      {error && (
        <div className="error-message">
          <i className="fas fa-exclamation-triangle icon"></i>
          {error}
        </div>
      )}

      {/* Step 1: Fetch Store */}
      <div className="form-group">
        <div className="input-group">
          <input 
            className="form-input"
            name="id" 
            placeholder="🔍 Enter Store ID to Update" 
            value={storeData.id}
            onChange={handleChange}
            disabled={storeFetched}
            required 
          />
          <div className="tooltip">
            <button 
              type="button"
              onClick={handleFetchStore}
              disabled={fetchLoading || storeFetched}
              className="btn btn-secondary"
            >
              {fetchLoading ? (
                <>
                  <span className="loading-spinner"></span>
                  Fetching...
                </>
              ) : storeFetched ? (
                <>
                  <i className="fas fa-check icon"></i>
                  Loaded
                </>
              ) : (
                <>
                  <i className="fas fa-download icon"></i>
                  Fetch
                </>
              )}
            </button>
            <span className="tooltiptext">Click to load store details for editing</span>
          </div>
        </div>
      </div>

      {/* Step 2: Update Form (only show after fetching) */}
      {storeFetched && (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input 
              className="form-input"
              name="storeName" 
              placeholder="🏪 Update Store Name" 
              value={storeData.storeName}
              onChange={handleChange}
              required 
            />
          </div>
          
          <div className="form-group">
            <input 
              className="form-input"
              name="address" 
              placeholder="📍 Update Full Address" 
              value={storeData.address}
              onChange={handleChange}
              required 
            />
          </div>
          
          <div className="form-group">
            <input 
              className="form-input"
              name="managerName" 
              placeholder="👤 Update Manager's Name" 
              value={storeData.managerName}
              onChange={handleChange}
              required 
            />
          </div>
          
          <div style={{ display: 'flex', gap: '15px' }}>
            <div className="tooltip" style={{ flex: 1 }}>
              <button 
                type="submit" 
                className="btn btn-update"
                disabled={loading}
                style={{ width: '100%' }}
              >
                {loading ? (
                  <>
                    <span className="loading-spinner"></span>
                    Updating Store...
                  </>
                ) : (
                  <>
                    <i className="fas fa-save icon"></i>
                    Save Changes
                  </>
                )}
              </button>
              <span className="tooltiptext">Save all changes to the store</span>
            </div>
            
            <div className="tooltip">
              <button 
                type="button"
                onClick={handleReset}
                className="btn btn-secondary"
                disabled={loading}
              >
                <i className="fas fa-undo icon"></i>
                Reset
              </button>
              <span className="tooltiptext">Clear form and start over</span>
            </div>
          </div>
        </form>
      )}

      {/* Helpful instructions */}
      {!storeFetched && !error && (
        <div style={{ 
          marginTop: '20px', 
          padding: '15px', 
          background: 'linear-gradient(135deg, rgba(69,183,209,0.1), rgba(116,199,220,0.1))',
          borderRadius: '15px',
          border: '2px solid rgba(69,183,209,0.2)',
          color: '#45b7d1',
          textAlign: 'center',
          fontWeight: '500'
        }}>
          <i className="fas fa-info-circle icon"></i>
          💡 Enter a Store ID above and click "Fetch" to load store details for editing
        </div>
      )}
    </div>
  );
}

export default UpdateStoreForm;