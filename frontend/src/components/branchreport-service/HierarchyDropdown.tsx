"use client";

import React, { useMemo } from "react";
import { Area, SubArea, Branch } from "@/services/region-service/regionService";
import { BranchreportUserAssignment } from "@/services/branchreport-service/userService";

// Common types
type FilterValue = string | "all";

// Base interfaces
export interface HierarchyDropdownProps {
  areas: Area[];
  subAreas: SubArea[];
  branches: Branch[];
  currentUserAssignments: BranchreportUserAssignment[];
  disabled?: boolean;
  className?: string;
}

// Multi-select checkbox component props
export interface MultiSelectHierarchyProps extends HierarchyDropdownProps {
  selectedAreaIds: string[];
  selectedSubAreaIds: string[];
  selectedBranchIds: string[];
  onAreaChange: (areaIds: string[]) => void;
  onSubAreaChange: (subAreaIds: string[]) => void;
  onBranchChange: (branchIds: string[]) => void;
  showAreas?: boolean;
  showSubAreas?: boolean;
  showBranches?: boolean;
}

// Single-select dropdown component props
export interface SingleSelectHierarchyProps extends HierarchyDropdownProps {
  selectedAreaId?: string;
  selectedSubAreaId?: string | "all";
  selectedBranchId?: string;
  onAreaChange: (areaId?: string) => void;
  onSubAreaChange: (subAreaId?: string | "all") => void;
  onBranchChange: (branchId?: string) => void;
  showAreas?: boolean;
  showSubAreas?: boolean;
  showBranches?: boolean;
  allowAll?: boolean; // For "all" option in filters
}

// Filter dropdown component props
export interface FilterHierarchyProps extends HierarchyDropdownProps {
  areaFilter: FilterValue;
  subAreaFilter: FilterValue;
  branchFilter: FilterValue;
  onAreaFilterChange: (value: FilterValue) => void;
  onSubAreaFilterChange: (value: FilterValue) => void;
  onBranchFilterChange: (value: FilterValue) => void;
  showAreas?: boolean;
  showSubAreas?: boolean;
  showBranches?: boolean;
}

// Utility function to toggle selection
export const toggleSelection = (prev: string[], value: string): string[] => {
  return prev.includes(value)
    ? prev.filter((id) => id !== value)
    : [...prev, value];
};

// Utility to filter by user assignments - shows all data for users without assignments
export const filterByAssignments = {
  area: (areas: Area[], assignments: BranchreportUserAssignment[]) => {
    // If no assignments, show all areas (admin user)
    if (assignments.length === 0) return areas;
    return areas.filter(area => assignments.some(a => a.areaId === area.id));
  },

  subArea: (subAreas: SubArea[], assignments: BranchreportUserAssignment[]) => {
    // If no assignments, show all sub-areas (admin user)
    if (assignments.length === 0) return subAreas;
    return subAreas.filter(subArea => assignments.some(a => a.subAreaId === subArea.id));
  },

  branch: (branches: Branch[], assignments: BranchreportUserAssignment[]) => {
    // If no assignments, show all branches (admin user)
    if (assignments.length === 0) return branches;
    return branches.filter(branch => assignments.some(a => a.branchId === branch.id));
  },
};

// Multi-select checkbox component
// Simple single-select dropdown for area-branch page
export interface SimpleHierarchyDropdownProps {
  type: "area" | "subarea" | "branch";
  value?: string;
  onChange: (value?: string) => void;
  areas: Area[];
  subAreas: SubArea[];
  branches: Branch[];
  placeholder?: string;
  disabled?: boolean;
  className?: string;
}

// Simple filter dropdown for area-branch page
export interface SimpleFilterHierarchyDropdownProps {
  type: "area" | "subarea" | "branch";
  value: FilterValue;
  onChange: (value: FilterValue) => void;
  areas: Area[];
  subAreas: SubArea[];
  branches: Branch[];
  placeholder?: string;
  className?: string;
}

// Option type for dropdown items
type Option = {
  value: string;
  label: string;
  parentArea?: string;
  parentSubArea?: string;
};

// Single-select dropdown component
export function HierarchyDropdown({
  type,
  value,
  onChange,
  areas,
  subAreas,
  branches,
  placeholder = "Select option",
  disabled = false,
  className = "",
}: SimpleHierarchyDropdownProps) {
  const baseClassName = "w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50";

  const getOptions = (): Option[] => {
    switch (type) {
      case "area":
        return areas.map((area: Area) => ({ value: area.id, label: area.name }));
      case "subarea":
        return subAreas.map((subArea: SubArea) => ({
          value: subArea.id,
          label: subArea.name,
          parentArea: areas.find((a: Area) => a.id === subArea.area_id)?.name
        }));
      case "branch":
        return branches.map((branch: Branch) => ({
          value: branch.id,
          label: branch.name,
          parentArea: areas.find((a: Area) => a.id === branch.area_id)?.name,
          parentSubArea: subAreas.find((s: SubArea) => s.id === branch.sub_area_id)?.name
        }));
      default:
        return [];
    }
  };

  const options = getOptions();

  return (
    <select
      value={value || ""}
      onChange={(e) => onChange(e.target.value || undefined)}
      disabled={disabled}
      className={`${baseClassName} ${className}`}
    >
      <option value="">{placeholder}</option>
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.label}
          {option.parentArea && ` (${option.parentArea}`}
          {option.parentSubArea && ` / ${option.parentSubArea}`}
          {option.parentArea && ")"}
        </option>
      ))}
    </select>
  );
}

// Filter dropdown component
export function FilterHierarchyDropdown({
  type,
  value,
  onChange,
  areas,
  subAreas,
  branches,
  placeholder = "All options",
  className = "",
}: SimpleFilterHierarchyDropdownProps) {
  const baseClassName = "w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500";

  const getOptions = (): Option[] => {
    switch (type) {
      case "area":
        return areas.map((area: Area) => ({ value: area.id, label: area.name }));
      case "subarea":
        return subAreas.map((subArea: SubArea) => ({
          value: subArea.id,
          label: subArea.name,
          parentArea: areas.find((a: Area) => a.id === subArea.area_id)?.name
        }));
      case "branch":
        return branches.map((branch: Branch) => ({
          value: branch.id,
          label: branch.name,
          parentArea: areas.find((a: Area) => a.id === branch.area_id)?.name,
          parentSubArea: subAreas.find((s: SubArea) => s.id === branch.sub_area_id)?.name
        }));
      default:
        return [];
    }
  };

  const options = getOptions();

  return (
    <select
      value={value}
      onChange={(e) => onChange(e.target.value === "all" ? "all" : e.target.value)}
      className={`${baseClassName} ${className}`}
    >
      <option value="all">{placeholder}</option>
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.label}
          {option.parentArea && ` (${option.parentArea}`}
          {option.parentSubArea && ` / ${option.parentSubArea}`}
          {option.parentArea && ")"}
        </option>
      ))}
    </select>
  );
}

export function MultiSelectHierarchyDropdown({
  areas,
  subAreas,
  branches,
  currentUserAssignments,
  selectedAreaIds,
  selectedSubAreaIds,
  selectedBranchIds,
  onAreaChange,
  onSubAreaChange,
  onBranchChange,
  showAreas = true,
  showSubAreas = true,
  showBranches = true,
  disabled = false,
  className = "",
}: MultiSelectHierarchyProps) {

  // Filter hierarchy based on current user's assignments
  const filteredAreas = useMemo(() => {
    return filterByAssignments.area(areas, currentUserAssignments);
  }, [areas, currentUserAssignments]);

  const filteredSubAreas = useMemo(() => {
    return filterByAssignments.subArea(subAreas, currentUserAssignments);
  }, [subAreas, currentUserAssignments]);

  const filteredBranches = useMemo(() => {
    return filterByAssignments.branch(branches, currentUserAssignments);
  }, [branches, currentUserAssignments]);

  const handleAreaToggle = (areaId: string) => {
    const newSelected = toggleSelection(selectedAreaIds, areaId);
    onAreaChange(newSelected);
  };

  const handleSubAreaToggle = (subAreaId: string) => {
    const newSelected = toggleSelection(selectedSubAreaIds, subAreaId);
    onSubAreaChange(newSelected);
  };

  const handleBranchToggle = (branchId: string) => {
    const newSelected = toggleSelection(selectedBranchIds, branchId);
    onBranchChange(newSelected);
  };

  return (
    <div className={`space-y-4 ${className}`}>
      {showAreas && filteredAreas.length > 0 && (
        <div>
          <h4 className="text-sm font-medium text-slate-200 mb-2">Areas</h4>
          <div className="space-y-2 max-h-40 overflow-y-auto border border-slate-600 rounded p-3 bg-slate-700/40">
            {filteredAreas.map((area) => (
              <label key={area.id} className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={selectedAreaIds.includes(area.id)}
                  onChange={() => handleAreaToggle(area.id)}
                  disabled={disabled}
                  className="rounded border-slate-500 text-blue-500 focus:ring-blue-400"
                />
                <span className="text-sm text-slate-100">{area.name}</span>
              </label>
            ))}
          </div>
        </div>
      )}

      {showSubAreas && filteredSubAreas.length > 0 && (
        <div>
          <h4 className="text-sm font-medium text-slate-200 mb-2">Sub-Areas</h4>
          <div className="space-y-2 max-h-40 overflow-y-auto border border-slate-600 rounded p-3 bg-slate-700/40">
            {filteredSubAreas.map((subArea) => (
              <label key={subArea.id} className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={selectedSubAreaIds.includes(subArea.id)}
                  onChange={() => handleSubAreaToggle(subArea.id)}
                  disabled={disabled}
                  className="rounded border-slate-500 text-blue-500 focus:ring-blue-400"
                />
                <span className="text-sm text-slate-100">{subArea.name}</span>
                <span className="text-xs text-slate-400">
                  ({areas.find(a => a.id === subArea.area_id)?.name})
                </span>
              </label>
            ))}
          </div>
        </div>
      )}

      {showBranches && filteredBranches.length > 0 && (
        <div>
          <h4 className="text-sm font-medium text-slate-200 mb-2">Branches</h4>
          <div className="space-y-2 max-h-40 overflow-y-auto border border-slate-600 rounded p-3 bg-slate-700/40">
            {filteredBranches.map((branch) => (
              <label key={branch.id} className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={selectedBranchIds.includes(branch.id)}
                  onChange={() => handleBranchToggle(branch.id)}
                  disabled={disabled}
                  className="rounded border-slate-500 text-blue-500 focus:ring-blue-400"
                />
                <span className="text-sm text-slate-100">{branch.name}</span>
                <span className="text-xs text-slate-400">
                  ({areas.find(a => a.id === branch.area_id)?.name} /
                  {subAreas.find(s => s.id === branch.sub_area_id)?.name})
                </span>
              </label>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
