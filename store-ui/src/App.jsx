import React from 'react';
import UpdateStoreForm from './components/UpdateStoreForm';
import DeleteStore from './components/DeleteStore';
import StoreDetails from './components/StoreDetails';
import CreateStoreForm from './components/CreateStoreForm';
import './App.css';

function App() {
  return (
    <div className="app-container">
      <header className="app-header">
        <h1 className="app-title">
          <i className="fas fa-store-alt icon"></i>
          Store Management System
        </h1>
        <p className="app-subtitle">
          <i className="fas fa-sparkles icon"></i>
          Manage your stores with style, ease and efficiency
          <i className="fas fa-heart icon" style={{color: '#ff6b6b', marginLeft: '10px'}}></i>
        </p>
      </header>
      
      <div className="components-grid">
        <div className="component-card create-store">
          <CreateStoreForm />
        </div>
        
        <div className="component-card store-details">
          <StoreDetails />
        </div>
        
        <div className="component-card update-store">
          <UpdateStoreForm />
        </div>
        
        <div className="component-card delete-store">
          <DeleteStore />
        </div>
      </div>
    </div>
  );
}

export default App;