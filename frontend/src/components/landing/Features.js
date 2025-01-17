const Features = () => {
  return (
    <section className="px-4 py-20 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-7xl">
        <div className="space-y-32">
          <div className="grid grid-cols-1 items-center gap-12 lg:grid-cols-2">
            <div>
              <h2 className="mb-6 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
                The fastest way to plug your own data
              </h2>
              <p className="mb-8 text-lg text-gray-600">
                Build complex workflows with our intuitive node-based editor
              </p>
            </div>
            <img src="/assets/feature-1.png" className="aspect-video object-cover rounded-lg bg-gray-100"/>
          </div>

          <div className="grid grid-cols-1 items-center gap-12 lg:grid-cols-2">
            <img src="/assets/feature-2.png" className="aspect-video object-cover rounded-lg bg-gray-100 lg:order-first"/>
            <div>
              <h2 className="mb-6 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
                Change data context with ease
              </h2>
              <p className="mb-8 text-lg text-gray-600">
                Switch seamlessly between visual workflow builder and context
                editor
              </p>
            </div>
          </div>

        </div>
      </div>
    </section>
  );
};

export default Features;
