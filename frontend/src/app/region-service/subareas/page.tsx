"use client";

import { useState, useEffect } from "react";
import { regionService, SubArea, SubAreaPayload, Area } from "@/services/region-service/regionService";
import { useToast } from "@/components/ui/Toast";

type FormState<T> = Partial<T> & { id?: string };

const defaultSubAreaForm: FormState<SubAreaPayload> = {
  name: "",
  description: "",
  areaId: "",
};

export default function SubAreasPage() {
  const { showToast } = useToast();

  const [subAreas, setSubAreas] = useState<SubArea[]>([]);
  const [areas, setAreas] = useState<Area[]>([]);
  const [subAreaForm, setSubAreaForm] = useState<FormState<SubAreaPayload>>(defaultSubAreaForm);
  const [editingSubArea, setEditingSubArea] = useState<string | null>(null);
  const [loadingSubAreas, setLoadingSubAreas] = useState(false);
  const [loadingAreas, setLoadingAreas] = useState(false);
  const [submittingSubArea, setSubmittingSubArea] = useState(false);

  useEffect(() => {
    loadSubAreas();
    loadAreas();
  }, []);

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

  const handleCreateOrUpdateSubArea = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!subAreaForm.name || !subAreaForm.areaId) {
      showToast("Name and area are required", "error");
      return;
    }

    try {
      setSubmittingSubArea(true);

      if (editingSubArea) {
        await regionService.updateSubArea(editingSubArea, subAreaForm as SubAreaPayload);
        showToast("Sub area updated successfully", "success");
      } else {
        await regionService.createSubArea(subAreaForm as SubAreaPayload);
        showToast("Sub area created successfully", "success");
      }

      setSubAreaForm(defaultSubAreaForm);
      setEditingSubArea(null);
      loadSubAreas();
    } catch (error) {
      showToast(`Failed to ${editingSubArea ? "update" : "create"} sub area`, "error");
    } finally {
      setSubmittingSubArea(false);
    }
  };

  const handleEditSubArea = (subArea: SubArea) => {
    setSubAreaForm({
      name: subArea.name,
      description: subArea.description,
      areaId: subArea.areaId,
    });
    setEditingSubArea(subArea.id);
  };

  const handleDeleteSubArea = async (id: string) => {
    if (!confirm("Are you sure you want to delete this sub area?")) {
      return;
    }

    try {
      await regionService.deleteSubArea(id);
      showToast("Sub area deleted successfully", "success");
      loadSubAreas();
    } catch (error) {
      showToast("Failed to delete sub area", "error");
    }
  };

  const handleCancelSubAreaEdit = () => {
    setSubAreaForm(defaultSubAreaForm);
    setEditingSubArea(null);
  };

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-slate-100 mb-2">Sub Areas Management</h1>
        <p className="text-slate-400">Manage sub areas within regional areas</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Form Section */}
        <div className="bg-slate-800 rounded-lg p-6">
          <h2 className="text-xl font-semibold text-slate-100 mb-6">
            {editingSubArea ? "Edit Sub Area" : "Create Sub Area"}
          </h2>

          <form onSubmit={handleCreateOrUpdateSubArea} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Name *
              </label>
              <input
                type="text"
                value={subAreaForm.name || ""}
                onChange={(e) => setSubAreaForm({ ...subAreaForm, name: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter sub area name"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Area *
              </label>
              <select
                value={subAreaForm.areaId || ""}
                onChange={(e) => setSubAreaForm({ ...subAreaForm, areaId: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100"
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
                Description
              </label>
              <textarea
                value={subAreaForm.description || ""}
                onChange={(e) => setSubAreaForm({ ...subAreaForm, description: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter description"
                rows={3}
              />
            </div>

            <div className="flex space-x-2">
              <button
                type="submit"
                disabled={submittingSubArea}
                className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {submittingSubArea ? "Saving..." : (editingSubArea ? "Update" : "Create")}
              </button>
              {editingSubArea && (
                <button
                  type="button"
                  onClick={handleCancelSubAreaEdit}
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
          <h2 className="text-xl font-semibold text-slate-100 mb-6">Sub Areas List</h2>

          {loadingSubAreas ? (
            <div className="text-center py-8">
              <div className="text-slate-400">Loading sub areas...</div>
            </div>
          ) : subAreas.length === 0 ? (
            <div className="text-center py-8">
              <div className="text-slate-400">No sub areas found</div>
            </div>
          ) : (
            <div className="space-y-3">
              {subAreas.map((subArea) => (
                <div
                  key={subArea.id}
                  className="bg-slate-700 rounded-lg p-4 border border-slate-600"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center space-x-2">
                        <h3 className="font-medium text-slate-100">{subArea.name}</h3>
                      </div>
                      <p className="text-slate-400 text-sm mt-1">
                        Area: {subArea.areaId}
                      </p>
                      {subArea.description && (
                        <p className="text-slate-400 text-sm mt-1">{subArea.description}</p>
                      )}
                      <div className="text-slate-500 text-xs mt-2">
                        Created: {new Date(subArea.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                    <div className="flex space-x-2 ml-4">
                      <button
                        onClick={() => handleEditSubArea(subArea)}
                        className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteSubArea(subArea.id)}
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
