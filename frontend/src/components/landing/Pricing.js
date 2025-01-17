import { Link } from "react-router-dom";

const Pricing = () => {
    const creditPackages = [
      { name: "Starter", credits: 100, price: 100, popular: false },
      { name: "Pro", credits: 300, price: 250, popular: true },
      { name: "Enterprise", credits: 500, price: 400, popular: false },
    ];
  
    return (
      <section className="py-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
              Simple, Credits-Based Pricing
            </h2>
            <p className="mt-4 text-xl text-gray-600">
              Pay only for what you use with our flexible credit system
            </p>
          </div>
  
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {creditPackages.map((pkg) => (
              <div 
                key={pkg.name} 
                className={`bg-white p-8 rounded-lg shadow-sm border ${
                  pkg.popular ? 'border-teal-500' : 'border-gray-200'
                }`}
              >
                {pkg.popular && (
                  <span className="bg-teal-500 text-white px-3 py-1 rounded-full text-sm font-semibold mb-4 inline-block">
                    Most Popular
                  </span>
                )}
                <h3 className="text-2xl font-bold text-gray-900 mb-4">{pkg.name}</h3>
                <div className="text-4xl font-bold text-gray-900 mb-6">
                ₹ {pkg.price}
                </div>
                <ul className="text-gray-600 space-y-4 mb-8">
                  <li className="flex items-center">
                    <svg className="w-5 h-5 text-teal-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                    {pkg.credits.toLocaleString()} credits
                  </li>
                  <li className="flex items-center">
                    <svg className="w-5 h-5 text-teal-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                    Full access to all features
                  </li>
                  <li className="flex items-center">
                    <svg className="w-5 h-5 text-teal-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                    24/7 support
                  </li>
                </ul>
                <Link to={`/dashboard/payment?package=${pkg.name}`} className={`w-full py-3 px-4 rounded-md text-sm font-semibold ${
                  pkg.popular
                    ? 'bg-teal-500 text-white hover:bg-teal-600'
                    : 'bg-white text-teal-500 border border-teal-500 hover:bg-teal-50'
                }`}>
                  Choose {pkg.name}
                </Link>
              </div>
            ))}
          </div>
  
          <div className="mt-16 text-center">
            <h3 className="text-2xl font-bold text-gray-900 mb-4">How do credits work?</h3>
            <p className="text-gray-600 max-w-2xl mx-auto">
              Credits are consumed based on the complexity and frequency of your workflows. 
              One credit typically equals one workflow execution. Unused credits roll over 
              to the next month, ensuring you always get value for your investment.
            </p>
            <button className="mt-8 px-6 py-3 bg-teal-500 text-white rounded-md hover:bg-teal-600 transition-colors">
              Learn more about credits
            </button>
          </div>
        </div>
      </section>
    );
  }
  
  export default Pricing;