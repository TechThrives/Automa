import React, { useEffect } from "react";
import { useAppContext } from "../../context/AppContext";
import { FiMail, FiSmartphone } from "react-icons/fi";

const Profile = () => {
  const { user } = useAppContext();
  return (
    <div className="mb-6 flex w-full min-w-0 flex-col break-words rounded-lg bg-white shadow-xl">
      <div className="mt-6 px-6">
        <div className="flex flex-wrap justify-center">
          <div className="flex w-full justify-center px-4">
            <img
              alt="..."
              src={user.profileImageUrl}
              className="h-auto w-40 rounded-full border-none align-middle shadow-xl"
            />
          </div>
          <div className="w-full px-4 text-center">
            <div className="flex justify-center py-4 pt-8 lg:pt-4">
              <div className="mr-4 p-3 text-center">
                <span className="text-gray-600 block text-xl font-bold uppercase tracking-wide">
                  {user.workflows}
                </span>
                <span className="text-gray-400 text-sm">Workflows</span>
              </div>
              <div className="mr-4 p-3 text-center">
                <span className="text-gray-600 block text-xl font-bold uppercase tracking-wide">
                  {user.workflowRuns}
                </span>
                <span className="text-gray-400 text-sm">Workflow Runs</span>
              </div>
              <div className="p-3 text-center lg:mr-4">
                <span className="text-gray-600 block text-xl font-bold uppercase tracking-wide">
                  {user.credits}
                </span>
                <span className="text-gray-400 text-sm">Credits</span>
              </div>
            </div>
          </div>
        </div>
        <div className="mb-6 text-center">
          <h3 className="mb-2 text-xl font-semibold leading-normal text-gray-700">
            {user.firstName} {user.lastName}
          </h3>
          <div className="mb-2 mt-0 flex items-center justify-center text-sm font-bold uppercase leading-normal text-gray-400">
            <FiMail className="mr-2 h-4 w-4" />
            {user.email}
          </div>
          <div className="mb-2 mt-4 flex items-center justify-center text-gray-600">
            <FiSmartphone className="mr-2 h-4 w-4" />
            {user.phoneNumber}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
