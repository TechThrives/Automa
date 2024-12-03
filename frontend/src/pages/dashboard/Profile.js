import React, { useEffect } from "react";

const Profile = () => {
  return (
    <div className="flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-xl rounded-lg">
      <div className="px-6 mt-6">
        <div className="flex flex-wrap justify-center">
          <div className="w-full px-4 flex justify-center">
            <img
              alt="..."
              src="https://i.pinimg.com/originals/3b/2b/68/3b2b688987bfdcb685e3383d99a4212f.png"
              className="shadow-xl rounded-full h-auto w-40 align-middle border-none"
            />
          </div>
          <div className="w-full px-4 text-center">
            <div className="flex justify-center py-4 lg:pt-4 pt-8">
              <div className="mr-4 p-3 text-center">
                <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                  22
                </span>
                <span className="text-sm text-blueGray-400">Friends</span>
              </div>
              <div className="mr-4 p-3 text-center">
                <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                  10
                </span>
                <span className="text-sm text-blueGray-400">Photos</span>
              </div>
              <div className="lg:mr-4 p-3 text-center">
                <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                  89
                </span>
                <span className="text-sm text-blueGray-400">Comments</span>
              </div>
            </div>
          </div>
        </div>
        <div className="text-center mb-6">
          <h3 className="text-xl font-semibold leading-normal text-blueGray-700 mb-2">
            Jenna Stones
          </h3>
          <div className="text-sm leading-normal mt-0 mb-2 text-blueGray-400 font-bold uppercase">
            <i className="fas fa-map-marker-alt mr-2 text-lg text-blueGray-400"></i>
            Los Angeles, California
          </div>
          <div className="mb-2 text-blueGray-600 mt-4">
            <i className="fas fa-briefcase mr-2 text-lg text-blueGray-400"></i>
            Solution Manager - Creative Tim Officer
          </div>
          <div className="mb-2 text-blueGray-600">
            <i className="fas fa-university mr-2 text-lg text-blueGray-400"></i>
            University of Computer Science
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
