import { useState } from 'react';
import axios from 'axios';
import './StoreValidator.css';

function StoreValidator() {
  const [storeId, setStoreId] = useState('');
  const [statusColor, setStatusColor] = useState(null);
  const [issues, setIssues] = useState([]);
  const [jsonData, setJsonData] = useState(null);
  const [showJson, setShowJson] = useState(false);

  const checkStore = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/store/validate?storeId=${storeId}`);
      const data = res.data;

      setStatusColor(
        data.status === 'GREEN' ? 'green' :
        data.status === 'YELLOW' ? 'yellow' : 'red'
      );

      setIssues(data.issues || []);
      setJsonData(data.raw);
      setShowJson(false);
    } catch (err) {
      console.error(err);
      setIssues(['Error contacting backend']);
      setStatusColor('red');
    }
  };

  return (
    <div className="validator-container">
      <input
        type="text"
        placeholder="Enter Store ID"
        value={storeId}
        onChange={(e) => setStoreId(e.target.value)}
      />
      <button onClick={checkStore}>Validate</button>

      {statusColor && (
        <div className="status-dot" style={{ backgroundColor: statusColor }}></div>
      )}

      {issues.length > 0 && (
        <div className="issues">
          {issues.map((issue, idx) => (
            <p key={idx}>{issue}</p>
          ))}
        </div>
      )}

      <button onClick={() => setShowJson(!showJson)}>Toggle Full JSON</button>

      {showJson && jsonData && (
        <pre className="json-output">{JSON.stringify(jsonData, null, 2)}</pre>
      )}
    </div>
  );
}

export default StoreValidator;
