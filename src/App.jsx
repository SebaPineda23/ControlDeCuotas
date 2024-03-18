import FormNewSocio from './views/FormNewSocio'
import Home from './views/Home'
import { Routes,Route} from 'react-router-dom'
function App() {
  return (
    <div className='bg-gray-400 w-full min-h-screen flex items-center justify-center'>
      <Routes >
        <Route path="/" element={<Home />} />
        <Route path="/nuevoSocio" element={<FormNewSocio />} />
      </Routes>
    </div>
  )
}


export default App
