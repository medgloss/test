import { useState } from 'react'
import StoreValidator from './storeValidator'
StoreValidator
function App() {
  const [count, setCount] = useState(0)

  return (
    <div>
        <StoreValidator />
    </div>
  )
}

export default App
