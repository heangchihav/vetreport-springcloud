"use client";

import { useState, useEffect } from "react";
import { regionService, Branch, BranchPayload, SubArea, Area } from "@/services/region-service/regionService";
import { useToast } from "@/components/ui/Toast";

type FormState<T> = Partial<T> & { id?: string };

const defaultBranchForm: FormState<BranchPayload> = {
  name: "",
  description: "",
  areaId: "",
  subAreaId: "",
};

export default function BranchesPage() {
  const { showToast } = useToast();

  const [branches, setBranches] = useState<Branch[]>([]);
  const [subAreas, setSubAreas] = useState<SubArea[]>([]);
  const [areas, setAreas] = useState<Area[]>([]);
  const [branchForm, setBranchForm] = useState<FormState<BranchPayload>>(defaultBranchForm);
  const [editingBranch, setEditingBranch] = useState<string | null>(null);
  const [loadingBranches, setLoadingBranches] = useState(false);
  const [loadingSubAreas, setLoadingSubAreas] = useState(false);
  const [loadingAreas, setLoadingAreas] = useState(false);
  const [submittingBranch, setSubmittingBranch] = useState(false);

  useEffect(() => {
    loadBranches();
    loadSubAreas();
    loadAreas();
  }, []);

  const loadBranches = async () => {
    try {
      setLoadingBranches(true);
      const data = await regionService.listBranches();
      setBranches(data);
    } catch (error) {
      showToast("Failed to load branches", "error");
    } finally {
      setLoadingBranches(false);
    }
  };

  const loadSubAreas = async () => {
    try {
      setLoadingSubAreas(true);
      const data = await regionService.listSubAreas();
      setSubAreas(data);
    } catch (error) {
      showToast("Failed to load sub areas", "error");
    } finally {
      setLoadingSubAreas(false);
    }
  };

  const loadAreas = async () => {
    try {
      setLoadingAreas(true);
      const data = await regionService.listAreas();
      setAreas(data);
    } catch (error) {
      showToast("Failed to load areas", "error");
    } finally {
      setLoadingAreas(false);
    }
  };

  const handleCreateOrUpdateBranch = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!branchForm.name || !branchForm.areaId || !branchForm.subAreaId) {
      showToast("Name, area, and sub area are required", "error");
      return;
    }

    try {
      setSubmittingBranch(true);

      if (editingBranch) {
        await regionService.updateBranch(editingBranch, branchForm as BranchPayload);
        showToast("Branch updated successfully", "success");
      } else {
        await regionService.createBranch(branchForm as BranchPayload);
        showToast("Branch created successfully", "success");
      }

      setBranchForm(defaultBranchForm);
      setEditingBranch(null);
      loadBranches();
    } catch (error) {
      showToast(`Failed to ${editingBranch ? "update" : "create"} branch`, "error");
    } finally {
      setSubmittingBranch(false);
    }
  };

  const handleEditBranch = (branch: Branch) => {
    setBranchForm({
      name: branch.name,
      description: branch.description,
      areaId: branch.areaId,
      subAreaId: branch.subAreaId,
    });
    setEditingBranch(branch.id);
  };

  const handleDeleteBranch = async (id: string) => {
    if (!confirm("Are you sure you want to delete this branch?")) {
      return;
    }

    try {
      await regionService.deleteBranch(id);
      showToast("Branch deleted successfully", "success");
      loadBranches();
    } catch (error) {
      showToast("Failed to delete branch", "error");
    }
  };

  const handleCancelBranchEdit = () => {
    setBranchForm(defaultBranchForm);
    setEditingBranch(null);
  };

  const getSubAreasByArea = (areaId: string) => {
    return subAreas.filter(subArea => subArea.areaId === areaId);
  };

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-slate-100 mb-2">Branches Management</h1>
        <p className="text-slate-400">Manage branch locations and information</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Form Section */}
        <div className="bg-slate-800 rounded-lg p-6">
          <h2 className="text-xl font-semibold text-slate-100 mb-6">
            {editingBranch ? "Edit Branch" : "Create Branch"}
          </h2>

          <form onSubmit={handleCreateOrUpdateBranch} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Name *
              </label>
              <input
                type="text"
                value={branchForm.name || ""}
                onChange={(e) => setBranchForm({ ...branchForm, name: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter branch name"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Area *
              </label>
              <select
                value={branchForm.areaId || ""}
                onChange={(e) => {
                  setBranchForm({
                    ...branchForm,
                    areaId: e.target.value,
                    subAreaId: "" // Reset sub area when area changes
                  });
                }}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 mb-2"
                required
              >
                <option value="">Select an area</option>
                {areas.map((area) => (
                  <option key={area.id} value={area.id}>
                    {area.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Sub Area *
              </label>
              <select
                value={branchForm.subAreaId || ""}
                onChange={(e) => setBranchForm({ ...branchForm, subAreaId: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100"
                required
                disabled={!branchForm.areaId}
              >
                <option value="">Select a sub area</option>
                {getSubAreasByArea(branchForm.areaId || "").map((subArea) => (
                  <option key={subArea.id} value={subArea.id}>
                    {subArea.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Description
              </label>
              <textarea
                value={branchForm.description || ""}
                onChange={(e) => setBranchForm({ ...branchForm, description: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter description"
                rows={3}
              />
            </div>

            <div className="flex space-x-2">
              <button
                type="submit"
                disabled={submittingBranch}
                className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {submittingBranch ? "Saving..." : (editingBranch ? "Update" : "Create")}
              </button>
              {editingBranch && (
                <button
                  type="button"
                  onClick={handleCancelBranchEdit}
                  className="px-4 py-2 bg-slate-600 text-white rounded-md hover:bg-slate-700 transition-colors"
                >
                  Cancel
                </button>
              )}
            </div>
          </form>
        </div>

        {/* List Section */}
        <div className="bg-slate-800 rounded-lg p-6">
          <h2 className="text-xl font-semibold text-slate-100 mb-6">Branches List</h2>

          {loadingBranches ? (
            <div className="text-center py-8">
              <div className="text-slate-400">Loading branches...</div>
            </div>
          ) : branches.length === 0 ? (
            <div className="text-center py-8">
              <div className="text-slate-400">No branches found</div>
            </div>
          ) : (
            <div className="space-y-3">
              {branches.map((branch) => (
                <div
                  key={branch.id}
                  className="bg-slate-700 rounded-lg p-4 border border-slate-600"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center space-x-2">
                        <h3 className="font-medium text-slate-100">{branch.name}</h3>
                      </div>
                      <p className="text-slate-400 text-sm mt-1">
                        Area: {branch.areaId} / Sub Area: {branch.subAreaId}
                      </p>
                      {branch.description && (
                        <p className="text-slate-400 text-sm mt-1">{branch.description}</p>
                      )}
                      <div className="text-slate-500 text-xs mt-2">
                        Created: {new Date(branch.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                    <div className="flex space-x-2 ml-4">
                      <button
                        onClick={() => handleEditBranch(branch)}
                        className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteBranch(branch.id)}
                        className="px-3 py-1 bg-red-600 text-white text-sm rounded hover:bg-red-700 transition-colors"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
