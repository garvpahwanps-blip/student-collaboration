import './App.css'
function App() {
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
          <button className="login-button" type="button">
            Log in
          </button>
        </header>
        <main className="hero-section">
          <p className="eyebrow">Collaborate with purpose</p>
          <h1>Find teammates who share your goals.</h1>
          <p className="hero-copy">
            Discover students with complementary skills,create collaboration requests,and build better projects together.
          </p>
          <div className="hero-actions">
            <button className="primary-button" type="button">
              Get started
            </button>
            <button className="secondary-button" type="button">
              Explore requests
            </button>
          </div>
        </main>
      </div>
  )
}
export default App;