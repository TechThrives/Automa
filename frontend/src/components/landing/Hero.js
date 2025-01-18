import { Link } from "react-router-dom";

export default function Hero() {
    return (
      <section className="pt-24 pb-16 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto text-center">
          <div className="flex justify-center mb-8">
            <Link to="https://github.com/TechThrives/automa" target="_blank" className="inline-flex items-center px-4 py-2 rounded-full bg-gray-100 text-sm text-gray-800">
              <span className="mr-2">🎯</span>
              Contribute to Automa
            </Link>
          </div>
          <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold tracking-tight text-gray-900 mb-8">
            Secure, easy<br />
            <span className="text-teal-500">workflow automation</span>
          </h1>
          <p className="max-w-2xl mx-auto text-lg sm:text-xl text-gray-600 mb-12">
            Build automations 10x faster with our intuitive workflow editor
          </p>
          <div className="flex flex-col sm:flex-row justify-center gap-4">
            <Link to="/signup" className="px-6 py-3 text-white bg-teal-500 hover:bg-teal-600 rounded-md text-base font-medium">
              Get started free
            </Link>
            <button className="px-6 py-3 text-gray-700 border border-gray-300 hover:bg-gray-50 rounded-md text-base font-medium">
              See Docs
            </button>
          </div>
        </div>
      </section>
    )
  }
  