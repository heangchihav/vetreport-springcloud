"use client";

import { useState, useEffect } from "react";
import { regionService, Area, AreaPayload } from "@/services/region-service/regionService";
import { useToast } from "@/components/ui/Toast";

type FormState<T> = Partial<T> & { id?: string };

const defaultAreaForm: FormState<AreaPayload> = {
  name: "",
  description: "",
};

export default function AreasPage() {
  const { showToast } = useToast();

  const [areas, setAreas] = useState<Area[]>([]);
  const [areaForm, setAreaForm] = useState<FormState<AreaPayload>>(defaultAreaForm);
  const [editingArea, setEditingArea] = useState<string | null>(null);
  const [loadingAreas, setLoadingAreas] = useState(false);
  const [submittingArea, setSubmittingArea] = useState(false);

  useEffect(() => {
    loadAreas();
  }, []);

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

  const handleCreateOrUpdateArea = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!areaForm.name) {
      showToast("Name is required", "error");
      return;
    }

    try {
      setSubmittingArea(true);

      if (editingArea) {
        await regionService.updateArea(editingArea, areaForm as AreaPayload);
        showToast("Area updated successfully", "success");
      } else {
        await regionService.createArea(areaForm as AreaPayload);
        showToast("Area created successfully", "success");
      }

      setAreaForm(defaultAreaForm);
      setEditingArea(null);
      loadAreas();
    } catch (error) {
      showToast(`Failed to ${editingArea ? "update" : "create"} area`, "error");
    } finally {
      setSubmittingArea(false);
    }
  };

  const handleEditArea = (area: Area) => {
    setAreaForm({
      name: area.name,
      description: area.description,
    });
    setEditingArea(area.id);
  };

  const handleDeleteArea = async (id: string) => {
    if (!confirm("Are you sure you want to delete this area?")) {
      return;
    }

    try {
      await regionService.deleteArea(id);
      showToast("Area deleted successfully", "success");
      loadAreas();
    } catch (error) {
      showToast("Failed to delete area", "error");
    }
  };

  const handleCancelAreaEdit = () => {
    setAreaForm(defaultAreaForm);
    setEditingArea(null);
  };

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-slate-100 mb-2">Areas Management</h1>
        <p className="text-slate-400">Manage regional areas and zones</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Form Section */}
        <div className="bg-slate-800 rounded-lg p-6">
          <h2 className="text-xl font-semibold text-slate-100 mb-6">
            {editingArea ? "Edit Area" : "Create Area"}
          </h2>

          <form onSubmit={handleCreateOrUpdateArea} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Name *
              </label>
              <input
                type="text"
                value={areaForm.name || ""}
                onChange={(e) => setAreaForm({ ...areaForm, name: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter area name"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-200 mb-1">
                Description
              </label>
              <textarea
                value={areaForm.description || ""}
                onChange={(e) => setAreaForm({ ...areaForm, description: e.target.value })}
                className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 text-slate-100 placeholder-slate-400"
                placeholder="Enter description"
                rows={3}
              />
            </div>

            <div className="flex space-x-2">
              <button
                type="submit"
                disabled={submittingArea}
                className="flex-1 bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {submittingArea ? "Saving..." : (editingArea ? "Update" : "Create")}
              </button>
              {editingArea && (
                <button
                  type="button"
                  onClick={handleCancelAreaEdit}
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
          <h2 className="text-xl font-semibold text-slate-100 mb-6">Areas List</h2>

          {loadingAreas ? (
            <div className="text-center py-8">
              <div className="text-slate-400">Loading areas...</div>
            </div>
          ) : areas.length === 0 ? (
            <div className="text-center py-8">
              <div className="text-slate-400">No areas found</div>
            </div>
          ) : (
            <div className="space-y-3">
              {areas.map((area) => (
                <div
                  key={area.id}
                  className="bg-slate-700 rounded-lg p-4 border border-slate-600"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center space-x-2">
                        <h3 className="font-medium text-slate-100">{area.name}</h3>
                      </div>
                      {area.description && (
                        <p className="text-slate-400 text-sm mt-1">{area.description}</p>
                      )}
                      <div className="text-slate-500 text-xs mt-2">
                        Created: {new Date(area.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                    <div className="flex space-x-2 ml-4">
                      <button
                        onClick={() => handleEditArea(area)}
                        className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteArea(area.id)}
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
