import './App.css'
import { useEffect, useState } from 'react'
import {
  Link,
  Route,
  Routes,
  useNavigate,
} from 'react-router-dom'
function HomePage() {
  return (
      <div className="landing-page">
        <header className="site-header">
          <a className="brand" href="/">
            StudentCollab
          </a>
          <nav aria-label="Main navigation">
            <a href="#how-it-works">How it works</a>
            <a href="#features">Features</a>
          </nav>
          <Link className="login-button" to="/login">
            Log in
          </Link>
        </header>
        <main className="hero-section">
          <p className="eyebrow">Collaborate with purpose</p>
          <h1>Find teammates who share your goals.</h1>
          <p className="hero-copy">
            Discover students with complementary skills,create collaboration requests,and build better projects together.
          </p>
          <div className="hero-actions">
            <Link className="primary-button"  to="/signup">
              Get started
            </Link>
            <button className="secondary-button" type="button">
              Explore requests
            </button>
          </div>
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
                <p>Send a join request and start working with your new teammates.</p>
              </article>
            </div>
          </section>
          <section id="features" className="how-it-works">
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
                <h3>Collaboration Requests</h3>
                <p>Find open collaboration requests that match your interests.</p>
              </article>
              <article className="step-card">
                <span>03</span>
                <h3>Join requests</h3>
                <p>Interested in a collaboration request? You can send its creator a join request.</p>
              </article>
            </div>
          </section>
        </main>
      </div>
  )
}
function SignUpPage() {
  const [status, setStatus] = useState({
    type: '',
    message: '',
  })

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

    setFormData({
      ...formData,
      [name]: value,
    })
  }
  async function handleSubmit(event) {
    event.preventDefault()

    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      const response = await fetch(
          'http://localhost:8080/api/auth/register',
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
          },
      )

      const contentType = response.headers.get('content-type') || ''

      const result = contentType.includes('application/json')
          ? await response.json()
          : await response.text()

      if (!response.ok) {
        const message =
            typeof result === 'string'
                ? result
                : result && typeof result === 'object'
                    ? Object.values(result).join(', ')
                    : 'Registration failed'

        throw new Error(message)
      }

      setStatus({
        type: 'success',
        message: `Account created for ${result.fullName}.`,
      })
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.message,
      })
    } finally {
      setIsSubmitting(false)
    }
  }
  return (
      <main className="form-page">
        <Link className="brand" to="/">
          StudentCollab
        </Link>
        <section className="auth-card">
          <p className="eyebrow">Join StudentCollab</p>
          <h1>Create your account</h1>
          <p className="auth-copy">
            Start finding students who can build with you.
          </p>

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
              <select id="yearOfStudy" name="yearOfStudy" value={formData.yearOfStudy} onChange={handleChange} required>
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

            <button
                className="primary-button"
                type="submit"
                disabled={isSubmitting}
            >
              {isSubmitting ? 'Creating account...' : 'Create account'}
            </button>
          </form>
          {status.message && (
              <p className={`form-message ${status.type}`} role="alert">
                {status.message}
              </p>
          )}

          <p className="auth-switch">
            Already have an account? <Link to="/login">Log in</Link>
          </p>
        </section>
      </main>
  )
}
function LoginPage() {
  const [status, setStatus] = useState({
    type: '',
    message: '',
  })

  const [isSubmitting, setIsSubmitting] = useState(false)
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  })
  const navigate = useNavigate()

  function handleChange(event) {
    const { name, value } = event.target

    setFormData({
      ...formData,
      [name]: value,
    })
  }
  async function handleSubmit(event) {
    event.preventDefault()

    setStatus({ type: '', message: '' })
    setIsSubmitting(true)

    try {
      const response = await fetch(
          'http://localhost:8080/api/auth/login',
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
          },
      )

      const contentType = response.headers.get('content-type') || ''

      const result = contentType.includes('application/json')
          ? await response.json()
          : await response.text()

      if (!response.ok) {
        const message =
            typeof result === 'string'
                ? result
                : Object.values(result).join(', ')

        throw new Error(message || 'Login failed')
      }

      localStorage.setItem('accessToken', result.accessToken)
      localStorage.setItem('student', JSON.stringify(result.student))
      navigate('/dashboard')

      setStatus({
        type: 'success',
        message: `Welcome back, ${result.student.fullName}.`,
      })
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.message,
      })
    } finally {
      setIsSubmitting(false)
    }
  }
  return (
      <main className="form-page">
        <Link className="brand" to="/">
          StudentCollab
        </Link>

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

            <button
                className="primary-button"
                type="submit"
                disabled={isSubmitting}
            >
              {isSubmitting ? 'Logging in...' : 'Log in'}
            </button>
          </form>
          {status.message && (
              <p className={`form-message ${status.type}`} role="alert">
                {status.message}
              </p>
          )}
          <p className="auth-switch">
            Do not have an account?{' '}
            <Link to="/signup">Create one</Link>
          </p>
        </section>
      </main>
  )
}
function DashboardPage() {
  const navigate = useNavigate()
  const [student, setStudent] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    const token = localStorage.getItem('accessToken')

    if (!token) {
      navigate('/login')
      return
    }

    async function loadCurrentStudent() {
      try {
        const response = await fetch(
            'http://localhost:8080/api/auth/me',
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            },
        )

        if (response.status === 401) {
          localStorage.removeItem('accessToken')
          localStorage.removeItem('student')
          navigate('/login')
          return
        }

        if (!response.ok) {
          throw new Error('Could not load your profile')
        }

        const data = await response.json()
        setStudent(data)
      } catch (error) {
        setError(error.message)
      }
    }

    loadCurrentStudent()
  }, [navigate])

  if (error) {
    return (
        <main className="form-page">
          <section className="auth-card">
            <p className="form-message error">{error}</p>
          </section>
        </main>
    )
  }

  if (!student) {
    return (
        <main className="form-page">
          <section className="auth-card">
            <p>Loading your profile...</p>
          </section>
        </main>
    )
  }

  function handleLogout() {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('student')
    navigate('/login')
  }

  return (
      <main className="form-page">
        <section className="auth-card">
          <p className="eyebrow">StudentCollab</p>
          <h1>Welcome, {student.fullName}</h1>
          <p className="auth-copy">
            You are successfully logged in.
          </p>

          <p>Email: {student.email}</p>
          <p>University: {student.university}</p>

          <button
              className="primary-button"
              type="button"
              onClick={handleLogout}
          >
            Log out
          </button>
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
      </Routes>
  )
}


export default App
