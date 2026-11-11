import './App.css'
import { useEffect, useState } from 'react'
import {
  Link,
  Route,
  Routes,
  useNavigate,
  useParams,
} from 'react-router-dom'

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

function getToken() {
  return localStorage.getItem('accessToken')
}

function clearAuth() {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('student')
}

async function readResponse(response) {
  const contentType = response.headers.get('content-type') || ''

  return contentType.includes('application/json')
    ? response.json()
    : response.text()
}

function getErrorMessage(result, fallback) {
  if (typeof result === 'string' && result.trim()) {
    return result
  }

  if (result && typeof result === 'object') {
    const values = Object.values(result).filter(Boolean)
    if (values.length > 0) {
      return values.join(', ')
    }
  }

  return fallback
}

async function apiRequest(path, options = {}) {
  const response = await fetch(API_BASE + path, options)
  const result = await readResponse(response)

  if (!response.ok) {
    const error = new Error(getErrorMessage(result, 'Something went wrong'))
    error.status = response.status
    throw error
  }

  return result
}

function authenticatedRequest(path, options = {}) {
  const token = getToken()

  return apiRequest(path, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: 'Bearer ' + token,
    },
  })
}

function HomePage() {
  return (
    <div className="landing-page">
      <header className="site-header">
        <Link className="brand" to="/">
          StudentCollab
        </Link>

        <nav aria-label="Main navigation">
          <a href="#how-it-works">How it works</a>
          <a href="#features">Features</a>
        </nav>

        <Link className="login-button" to="/login">
          Log in
        </Link>
      </header>

      <main>
        <section className="hero-section">
          <p className="eyebrow">Collaborate with purpose</p>
          <h1>Find teammates who share your goals.</h1>
          <p className="hero-copy">
            Discover students with complementary skills, create collaboration
            requests, and build better projects together.
          </p>

          <div className="hero-actions">
            <Link className="primary-button" to="/signup">
              Get started
            </Link>
            <Link className="secondary-button" to="/dashboard">
              Explore requests
            </Link>
          </div>
        </section>

        <section id="how-it-works" className="how-it-works">
          <p className="eyebrow">How it works</p>
          <h2>Build a team in three simple steps.</h2>

          <div className="steps">
            <article className="step-card">
              <span>01</span>
              <h3>Create your profile</h3>
              <p>Add your skills, interests, and the goals you want to achieve.</p>
            </article>

            <article className="step-card">
              <span>02</span>
              <h3>Explore opportunities</h3>
              <p>Find open collaboration requests that match your interests.</p>
            </article>

            <article className="step-card">
              <span>03</span>
              <h3>Connect and build</h3>
              <p>Send a join request and start working with new teammates.</p>
            </article>
          </div>
        </section>

        <section id="features" className="how-it-works features-section">
          <p className="eyebrow">Features</p>
          <h2>What can you do?</h2>

          <div className="steps">
            <article className="step-card">
              <span>01</span>
              <h3>AI-generated skill suggestions</h3>
              <p>Add a goal, and AI will suggest the skills required to achieve it.</p>
            </article>

            <article className="step-card">
              <span>02</span>
              <h3>Collaboration requests</h3>
              <p>Find open collaboration requests that match your interests.</p>
            </article>

            <article className="step-card">
              <span>03</span>
              <h3>Join requests</h3>
              <p>Interested in a request? Send its creator a join request.</p>
            </article>
          </div>
        </section>
      </main>
    </div>
  )
}

function AuthLayout({ children }) {
  return (
    <main className="form-page">
      <Link className="brand" to="/">
        StudentCollab
      </Link>
      {children}
    </main>
  )
}

function FormMessage({ status }) {
  if (!status.message) {
    return null
  }

  return (
    <p className={['form-message', status.type].filter(Boolean).join(' ')} role="alert">
      {status.message}
    </p>
  )
}

function SignUpPage() {
  const [status, setStatus] = useState({ type: '', message: '' })
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    university: '',
    yearOfStudy: '',
    password: '',
  })

  function handleChange(event) {
    const { name, value } = event.target
    setFormData((current) => ({ ...current, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      const result = await apiRequest('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      setStatus({
        type: 'success',
        message: 'Account created for ' + result.fullName + '. You can log in now.',
      })

      setFormData({
        fullName: '',
        email: '',
        university: '',
        yearOfStudy: '',
        password: '',
      })
    } catch (error) {
      setStatus({ type: 'error', message: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthLayout>
      <section className="auth-card">
        <p className="eyebrow">Join StudentCollab</p>
        <h1>Create your account</h1>
        <p className="auth-copy">Start finding students who can build with you.</p>

        <form className="signup-form" onSubmit={handleSubmit}>
          <div className="form-field">
            <label htmlFor="fullName">Full name</label>
            <input
              id="fullName"
              name="fullName"
              type="text"
              placeholder="Enter your full name"
              value={formData.fullName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="email">Email address</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="university">University</label>
            <input
              id="university"
              name="university"
              type="text"
              placeholder="Your university name"
              value={formData.university}
              onChange={handleChange}
            />
          </div>

          <div className="form-field">
            <label htmlFor="yearOfStudy">Year of study</label>
            <select
              id="yearOfStudy"
              name="yearOfStudy"
              value={formData.yearOfStudy}
              onChange={handleChange}
              required
            >
              <option value="">Select your year</option>
              <option value="FIRST">First year</option>
              <option value="SECOND">Second year</option>
              <option value="THIRD">Third year</option>
              <option value="FOURTH">Fourth year</option>
            </select>
          </div>

          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              placeholder="At least 8 characters"
              value={formData.password}
              onChange={handleChange}
              required
              minLength={8}
            />
          </div>

          <button className="primary-button" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Creating account...' : 'Create account'}
          </button>
        </form>

        <FormMessage status={status} />

        <p className="auth-switch">
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </section>
    </AuthLayout>
  )
}

function LoginPage() {
  const navigate = useNavigate()
  const [status, setStatus] = useState({ type: '', message: '' })
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [formData, setFormData] = useState({ email: '', password: '' })

  function handleChange(event) {
    const { name, value } = event.target
    setFormData((current) => ({ ...current, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      const result = await apiRequest('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      })

      localStorage.setItem('accessToken', result.accessToken)
      localStorage.setItem('student', JSON.stringify(result.student))
      navigate('/dashboard')
    } catch (error) {
      setStatus({ type: 'error', message: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthLayout>
      <section className="auth-card">
        <p className="eyebrow">Welcome back</p>
        <h1>Log in to your account</h1>
        <p className="auth-copy">
          Continue building meaningful projects with your teammates.
        </p>

        <form className="signup-form" onSubmit={handleSubmit}>
          <div className="form-field">
            <label htmlFor="loginEmail">Email address</label>
            <input
              id="loginEmail"
              name="email"
              type="email"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="loginPassword">Password</label>
            <input
              id="loginPassword"
              name="password"
              type="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button className="primary-button" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Logging in...' : 'Log in'}
          </button>
        </form>

        <FormMessage status={status} />

        <p className="auth-switch">
          Do not have an account? <Link to="/signup">Create one</Link>
        </p>
      </section>
    </AuthLayout>
  )
}

function ProfileMenu({ student, onLogout }) {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <div className="profile-menu">
      <button
        className="profile-button"
        type="button"
        onClick={() => setIsOpen((current) => !current)}
        aria-expanded={isOpen}
        aria-label="Open profile menu"
      >
        <span className="user-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <circle cx="12" cy="8" r="3.5" />
            <path d="M5.5 20c.8-3.6 3-5.5 6.5-5.5s5.7 1.9 6.5 5.5" />
          </svg>
        </span>
        <span>{student.fullName}</span>
        <span className="menu-arrow" aria-hidden="true">⌄</span>
      </button>

      {isOpen && (
        <div className="profile-dropdown">
          <Link to="/dashboard" onClick={() => setIsOpen(false)}>
            Dashboard
          </Link>
          <button
            className="dropdown-logout"
            type="button"
            onClick={() => {
              setIsOpen(false)
              onLogout()
            }}
          >
            Log out
          </button>
        </div>
      )}
    </div>
  )
}

function DashboardHeader({ student, onLogout }) {
  return (
    <header className="dashboard-header">
      <Link className="brand" to="/dashboard">
        StudentCollab
      </Link>
      <ProfileMenu student={student} onLogout={onLogout} />
    </header>
  )
}

function LoadingState({ message = 'Loading...' }) {
  return <p className="loading-state">{message}</p>
}

function ErrorState({ message }) {
  return (
    <div className="state-card error-state" role="alert">
      <h2>Something went wrong</h2>
      <p>{message}</p>
      <Link className="secondary-button" to="/dashboard">Try again</Link>
    </div>
  )
}

function RequestCard({ request }) {
  return (
    <article className="request-card">
      <div className="request-card-topline">
        <span className="request-status">{request.status}</span>
        <span className="request-id">Request #{request.id}</span>
      </div>
      <h3>{request.title}</h3>
      <p>{request.description}</p>

      <div className="request-meta">
        <span>Goal {request.goalId}</span>
        <span>{request.weeklyHours} hrs/week</span>
        <span>{request.collaborationMode}</span>
      </div>

      <Link
        className="secondary-button request-details-link"
        to={'/collaboration-requests/' + request.id}
      >
        View details
      </Link>
    </article>
  )
}

function DashboardPage() {
  const navigate = useNavigate()
  const [student, setStudent] = useState(null)
  const [requests, setRequests] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const token = getToken()

    if (!token) {
      navigate('/login', { replace: true })
      return
    }

    Promise.all([
      authenticatedRequest('/api/auth/me'),
      authenticatedRequest('/api/collaboration-requests/open'),
    ])
      .then(([studentData, requestData]) => {
        setStudent(studentData)
        setRequests(Array.isArray(requestData) ? requestData : [])
      })
      .catch((requestError) => {
        if (requestError.status === 401) {
          clearAuth()
          navigate('/login', { replace: true })
          return
        }
        setError(requestError.message)
      })
      .finally(() => setIsLoading(false))
  }, [navigate])

  function handleLogout() {
    clearAuth()
    navigate('/login', { replace: true })
  }

  if (isLoading) {
    return (
      <main className="dashboard-page">
        <LoadingState message="Loading your workspace..." />
      </main>
    )
  }

  if (error || !student) {
    return (
      <main className="dashboard-page">
        <ErrorState message={error || 'Could not load your profile'} />
      </main>
    )
  }

  return (
    <main className="dashboard-page">
      <DashboardHeader student={student} onLogout={handleLogout} />

      <section className="dashboard-hero">
        <div>
          <p className="eyebrow">Your workspace</p>
          <h1>Find your next teammate.</h1>
          <p>Browse open collaboration requests and discover projects worth building.</p>
        </div>
        <div className="dashboard-stat">
          <strong>{requests.length}</strong>
          <span>open requests</span>
        </div>
      </section>

      <section className="dashboard-section">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Explore</p>
            <h2>Open collaboration requests</h2>
          </div>
          <span className="section-count">{requests.length} available</span>
        </div>

        {requests.length === 0 ? (
          <div className="empty-state">
            <h3>No open requests yet</h3>
            <p>New collaboration opportunities will appear here.</p>
          </div>
        ) : (
          <div className="request-grid">
            {requests.map((request) => (
              <RequestCard key={request.id} request={request} />
            ))}
          </div>
        )}
      </section>
    </main>
  )
}

function CollaborationRequestDetailsPage() {
  const navigate = useNavigate()
  const { id } = useParams()
  const [request, setRequest] = useState(null)
  const [message, setMessage] = useState('')
  const [status, setStatus] = useState({ type: '', message: '' })
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    if (!getToken()) {
      navigate('/login', { replace: true })
      return
    }

    authenticatedRequest('/api/collaboration-requests/' + id)
      .then(setRequest)
      .catch((error) => {
        if (error.status === 401) {
          clearAuth()
          navigate('/login', { replace: true })
          return
        }
        setStatus({ type: 'error', message: error.message })
      })
      .finally(() => setIsLoading(false))
  }, [id, navigate])

  async function handleJoinRequest(event) {
    event.preventDefault()
    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      await authenticatedRequest('/api/join-requests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          collaborationRequestId: Number(id),
          message,
        }),
      })
      setStatus({
        type: 'success',
        message: 'Join request sent successfully.',
      })
      setMessage('')
    } catch (error) {
      if (error.status === 401) {
        clearAuth()
        navigate('/login', { replace: true })
        return
      }
      setStatus({ type: 'error', message: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  if (isLoading) {
    return (
      <main className="dashboard-page">
        <LoadingState message="Loading request details..." />
      </main>
    )
  }

  if (!request) {
    return (
      <main className="dashboard-page">
        <ErrorState message={status.message || 'Request not found'} />
      </main>
    )
  }

  return (
    <main className="dashboard-page">
      <header className="dashboard-header">
        <Link className="brand" to="/dashboard">StudentCollab</Link>
        <Link className="secondary-button" to="/dashboard">Back to dashboard</Link>
      </header>

      <section className="detail-layout">
        <article className="detail-card">
          <div className="request-card-topline">
            <span className="request-status">{request.status}</span>
            <span className="request-id">Request #{request.id}</span>
          </div>
          <p className="eyebrow">Collaboration opportunity</p>
          <h1>{request.title}</h1>
          <p className="detail-description">{request.description}</p>

          <div className="detail-grid">
            <div>
              <span>Goal</span>
              <strong>#{request.goalId}</strong>
            </div>
            <div>
              <span>Weekly commitment</span>
              <strong>{request.weeklyHours} hours</strong>
            </div>
            <div>
              <span>Collaboration mode</span>
              <strong>{request.collaborationMode}</strong>
            </div>
            <div>
              <span>Created by</span>
              <strong>Student #{request.createdById}</strong>
            </div>
          </div>
        </article>

        <aside className="join-card">
          <p className="eyebrow">Interested?</p>
          <h2>Introduce yourself</h2>
          <p>Send a short message to the request creator.</p>

          <form className="signup-form" onSubmit={handleJoinRequest}>
            <div className="form-field">
              <label htmlFor="joinMessage">Message</label>
              <textarea
                id="joinMessage"
                value={message}
                onChange={(event) => setMessage(event.target.value)}
                placeholder="Tell them why you would be a good teammate"
                rows="5"
              />
            </div>

            <button className="primary-button" type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Sending...' : 'Send join request'}
            </button>
          </form>

          <FormMessage status={status} />
        </aside>
      </section>
    </main>
  )
}

function NotFoundPage() {
  return (
    <main className="form-page">
      <section className="auth-card state-card">
        <p className="eyebrow">404</p>
        <h1>Page not found</h1>
        <Link className="primary-button" to="/">Return home</Link>
      </section>
    </main>
  )
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route
        path="/collaboration-requests/:id"
        element={<CollaborationRequestDetailsPage />}
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default App
