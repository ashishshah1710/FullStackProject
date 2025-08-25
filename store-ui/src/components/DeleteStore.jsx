import React, { useState } from 'react';
import { deleteStore, getStore } from '../api/storeService';

function DeleteStore() {
  const [storeId, setStoreId] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [storeDetails, setStoreDetails] = useState(null);
  const [fetchLoading, setFetchLoading] = useState(false);

  const handleInputChange = (e) => {
    setStoreId(e.target.value);
    if (success) setSuccess(false);
    if (error) setError('');
    if (storeDetails) setStoreDetails(null);
  };

  const handlePreviewStore = async () => {
    if (!storeId.trim()) {
      setError('Please enter a store ID first');
      return;
    }

    setFetchLoading(true);
    setError('');
    
    try {
      const response = await getStore(storeId);
      setStoreDetails(response.data);
    } catch (error) {
      console.error('Error fetching store:', error);
      setError('❌ Store not found. Please check the ID and try again.');
      setStoreDetails(null);
    } finally {
      setFetchLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!storeId.trim()) {
      setError('Please enter a store ID');
      return;
    }

    // Enhanced confirmation dialog
    const confirmMessage = storeDetails 
      ? `⚠️ Are you absolutely sure you want to delete "${storeDetails.storeName}"?\n\nThis action cannot be undone and will permanently remove:\n• Store Name: ${storeDetails.storeName}\n• Address: ${storeDetails.address}\n• Manager: ${storeDetails.managerName}\n\nType "DELETE" to confirm:`
      : '⚠️ Are you sure you want to delete this store? This action cannot be undone.';

    const userConfirmation = storeDetails 
      ? prompt(confirmMessage)
      : confirm(confirmMessage);

    if (storeDetails && userConfirmation !== 'DELETE') {
      setError('❌ Deletion cancelled. You must type "DELETE" to confirm.');
      return;
    }

    if (!storeDetails && !userConfirmation) {
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      await deleteStore(storeId);
      setSuccess(true);
      setStoreId('');
      setStoreDetails(null);
      
      // Auto-hide success message after 4 seconds
      setTimeout(() => setSuccess(false), 4000);
    } catch (error) {
      console.error('Error deleting store:', error);
      setError('❌ Error deleting store. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="component-title">
        <i className="fas fa-trash-alt icon"></i>
        Delete Store
      </h2>
      
      {success && (
        <div className="success-message">
          <i className="fas fa-check-circle icon"></i>
          🗑️ Store deleted successfully! The store has been permanently removed.
        </div>
      )}

      {error && (
        <div className="error-message">
          <i className="fas fa-exclamation-triangle icon"></i>
          {error}
        </div>
      )}
      
      <div className="form-group">
        <div className="input-group">
          <input 
            className="form-input"
            value={storeId} 
            onChange={handleInputChange} 
            placeholder="🗑️ Enter Store ID to Delete" 
          />
          <div className="tooltip">
            <button 
              onClick={handlePreviewStore}
              disabled={fetchLoading}
              className="btn btn-secondary"
            >
              {fetchLoading ? (
                <>
                  <span className="loading-spinner"></span>
                  Loading...
                </>
              ) : (
                <>
                  <i className="fas fa-eye icon"></i>
                  Preview
                </>
              )}
            </button>
            <span className="tooltiptext">Preview store details before deletion</span>
          </div>
        </div>
      </div>

      {/* Store Preview */}
      {storeDetails && (
        <div style={{
          background: 'linear-gradient(135deg, #ff6b6b, #ff8e8e)',
          borderRadius: '20px',
          padding: '20px',
          margin: '20px 0',
          color: 'white',
          border: '2px solid rgba(255,255,255,0.3)'
        }}>
          <h3 style={{ marginBottom: '15px', textAlign: 'center' }}>
            <i className="fas fa-exclamation-triangle icon"></i>
            Store to be Deleted
          </h3>
          <p><strong>🆔 ID:</strong> {storeDetails.id}</p>
          <p><strong>🏪 Store Name:</strong> {storeDetails.storeName}</p>
          <p><strong>📍 Address:</strong> {storeDetails.address}</p>
          <p><strong>👤 Manager:</strong> {storeDetails.managerName}</p>
        </div>
      )}
      
      <div className="tooltip">
        <button 
          onClick={handleDelete}
          disabled={loading}
          className="btn btn-danger"
          style={{ width: '100%' }}
        >
          {loading ? (
            <>
              <span className="loading-spinner"></span>
              Deleting Store...
            </>
          ) : (
            <>
              <i className="fas fa-fire icon"></i>
              {storeDetails ? 'Permanently Delete Store' : 'Delete Store'}
            </>
          )}
        </button>
        <span className="tooltiptext">
          {storeDetails 
            ? 'Permanently delete this store (requires typing DELETE to confirm)' 
            : 'Delete store by ID (requires confirmation)'
          }
        </span>
      </div>

      {/* Warning message */}
      <div style={{ 
        marginTop: '20px', 
        padding: '15px', 
        background: 'linear-gradient(135deg, rgba(240,147,251,0.1), rgba(245,169,252,0.1))',
        borderRadius: '15px',
        border: '2px solid rgba(240,147,251,0.2)',
        color: '#f093fb',
        textAlign: 'center',
        fontWeight: '500'
      }}>
        <i className="fas fa-shield-alt icon"></i>
        ⚠️ Warning: Deletion is permanent and cannot be undone. Use "Preview" to verify store details first.
      </div>
    </div>
  );
}

export default DeleteStore;